package com.balikllama.xpguiderdemo.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Activity-scoped ViewModel factory
 * Tüm ekranlarda aynı CreditViewModel instance'ını kullanmak için
 */
@Composable
inline fun <reified VM : ViewModel> activityViewModel(
    viewModelStoreOwner: ViewModelStoreOwner
): VM {
    return viewModel(
        viewModelStoreOwner = viewModelStoreOwner
    )
}

/**
 * CreditViewModel için extension function
 * Her ekrandan kolayca erişim için
 */
@Composable
fun rememberCreditViewModel(
    viewModelStoreOwner: ViewModelStoreOwner
): CreditViewModel {
    return activityViewModel(viewModelStoreOwner)
}