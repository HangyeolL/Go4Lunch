package com.hangyeollee.go4lunch.ui.workmates

import android.app.Application
import com.hangyeollee.go4lunch.data.repository.FirebaseRepository
import com.hangyeollee.go4lunch.data.repository.AutoCompleteDataRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import com.hangyeollee.go4lunch.ui.workmates.WorkmatesViewState
import androidx.lifecycle.LiveData
import com.hangyeollee.go4lunch.data.model.LunchRestaurant
import com.hangyeollee.go4lunch.ui.workmates.WorkmatesItemViewState
import com.hangyeollee.go4lunch.R
import com.hangyeollee.go4lunch.data.model.User
import java.util.ArrayList

class WorkmatesViewModel(
    private val context: Application,
    firebaseRepository: FirebaseRepository,
    autoCompleteDataRepository: AutoCompleteDataRepository
) : ViewModel() {
    private val workMatesFragmentViewStateMediatorLiveData = MediatorLiveData<WorkmatesViewState>()

    init {
        val userListLiveData = firebaseRepository.usersList
        val lunchRestaurantListLiveData = firebaseRepository.lunchRestaurantListOfAllUsers
        val userInputLiveData = autoCompleteDataRepository.userInputMutableLiveData

        workMatesFragmentViewStateMediatorLiveData.addSource(userListLiveData) { userList: List<User>? ->
            combine(userList, lunchRestaurantListLiveData.value, userInputLiveData.value)
        }
        workMatesFragmentViewStateMediatorLiveData.addSource(lunchRestaurantListLiveData) { lunchRestaurantList: List<LunchRestaurant>? ->
            combine(userListLiveData.value, lunchRestaurantList, userInputLiveData.value)
        }
        workMatesFragmentViewStateMediatorLiveData.addSource(userInputLiveData) { userInput: String? ->
            combine(userListLiveData.value, lunchRestaurantListLiveData.value, userInput)
        }
    }

    private fun combine(userList: List<User>?, lunchRestaurantList: List<LunchRestaurant>?, userInput: String?) {
        if (userList == null || lunchRestaurantList == null) {
            return
        }

        val recyclerViewItemViewStateList: List<WorkmatesItemViewState> = lunchRestaurantList.mapNotNull { lunchRestaurant ->
            val matchingUser = userList.find { it.id.equals(lunchRestaurant.userId, ignoreCase = true) } ?: return@mapNotNull null

            WorkmatesItemViewState(
                matchingUser.photoUrl,
                matchingUser.name,
                lunchRestaurant.restaurantName,
                lunchRestaurant.restaurantId
            )
        }


        val recyclerViewItemViewStateList: List<WorkmatesItemViewState> = ArrayList()
        val userIdsEatingToday: MutableList<String> = ArrayList()
        if (lunchRestaurantList != null) {
            for (lunchRestaurant in lunchRestaurantList) {
                var matchingUser: User? = null
                for (user in userList) {
                    if (user.id.equals(lunchRestaurant.userId, ignoreCase = true)) {
                        matchingUser = user
                        break
                    }
                }
                if (matchingUser != null) {
                    userIdsEatingToday.add(matchingUser.id)
                    if (userInput == null || matchingUser.name.contains(userInput) || lunchRestaurant.restaurantName.contains(userInput)) {
                        recyclerViewItemViewStateList.add(
                            WorkmatesItemViewState(
                                matchingUser.photoUrl,
                                matchingUser.name,
                                lunchRestaurant.restaurantName,
                                lunchRestaurant.restaurantId
                            )
                        )
                    }
                }
            }
        }
        for (user in userList) {
            if (!userIdsEatingToday.contains(user.id)) {
                if (userInput == null || user.name.contains(userInput)) {
                    recyclerViewItemViewStateList.add(
                        WorkmatesItemViewState(
                            user.photoUrl,
                            user.name,
                            context.getString(R.string.not_going_to_restaurant_yet),
                            null
                        )
                    )
                }
            }
        }
        workMatesFragmentViewStateMediatorLiveData.value = WorkmatesViewState(recyclerViewItemViewStateList)
    }

    val workmatesFragmentViewStateLiveData: LiveData<WorkmatesViewState>
        get() = workMatesFragmentViewStateMediatorLiveData
}