package com.bangkit.fingoal.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.fingoal.data.di.Injection
import com.bangkit.fingoal.data.repository.UserRepository
import com.bangkit.fingoal.view.activities.budget.BudgetFormViewModel
import com.bangkit.fingoal.view.activities.budget.EditBudgetViewModel
import com.bangkit.fingoal.view.activities.expense.ExpenseFormViewModel
import com.bangkit.fingoal.view.activities.goals.EditGoalViewModel
import com.bangkit.fingoal.view.activities.goals.GoalsFormViewModel
import com.bangkit.fingoal.view.activities.income.IncomeFormViewModel
import com.bangkit.fingoal.view.activities.login.LoginViewModel
import com.bangkit.fingoal.view.activities.profile.ProfileViewModel
import com.bangkit.fingoal.view.activities.register.RegisterViewModel
import com.bangkit.fingoal.view.activities.savings.SavingsFormViewModel
import com.bangkit.fingoal.view.budget.BudgetViewModel
import com.bangkit.fingoal.view.goals.GoalsViewModel
import com.bangkit.fingoal.view.home.HomeViewModel
import com.bangkit.fingoal.view.main.MainViewModel

class ViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(IncomeFormViewModel::class.java) -> {
                IncomeFormViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ExpenseFormViewModel::class.java) -> {
                ExpenseFormViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SavingsFormViewModel::class.java) -> {
                SavingsFormViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(BudgetViewModel::class.java) -> {
                BudgetViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(GoalsViewModel::class.java) -> {
                GoalsViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(GoalsFormViewModel::class.java) -> {
                GoalsFormViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(BudgetFormViewModel::class.java) -> {
                BudgetFormViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(EditBudgetViewModel::class.java) -> {
                EditBudgetViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(EditGoalViewModel::class.java) -> {
                EditGoalViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideUserRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}