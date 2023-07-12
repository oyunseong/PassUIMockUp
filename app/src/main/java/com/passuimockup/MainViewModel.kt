package com.passuimockup

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dataStore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val _successLoadData: MutableSharedFlow<Unit> = MutableSharedFlow()
    val successLoadData: SharedFlow<Unit> = _successLoadData.asSharedFlow()

    val nickname: MutableStateFlow<String> = MutableStateFlow("")
    val profile: MutableStateFlow<String> = MutableStateFlow("")
    val sex: MutableStateFlow<String> = MutableStateFlow("")
    val cardNumber: MutableStateFlow<String> = MutableStateFlow("")
    val address: MutableStateFlow<String> = MutableStateFlow("")
    val birthday: MutableStateFlow<String> = MutableStateFlow("")
    val renewal: MutableStateFlow<String> = MutableStateFlow("")
    val idNumber: MutableStateFlow<String> = MutableStateFlow("")
    val placeOfIssue: MutableStateFlow<String> = MutableStateFlow("")
    val dateOfIssue: MutableStateFlow<String> = MutableStateFlow("")

    val IMAGE_KEY = stringPreferencesKey("image")
    val NICKNAME_KEY = stringPreferencesKey("nickname")
    val CARD_NUMBER_KEY = stringPreferencesKey("cardNumber")
    val ADDRESS_KEY = stringPreferencesKey("address")
    val BIRTHDAY_KEY = stringPreferencesKey("birthday")
    val RENEWAL_KEY = stringPreferencesKey("renewal")
    val ID_NUMBER_KEY = stringPreferencesKey("idNumber")
    val PLACE_KEY = stringPreferencesKey("placeOfIssue")
    val DATE_KEY = stringPreferencesKey("dateOfIssue")
    val SEX_KEY = stringPreferencesKey("sex")

    init {
        viewModelScope.launch {
            // 이미지
            loadData(IMAGE_KEY, Type.IMAGE)
            loadData(NICKNAME_KEY, Type.NICKNAME)
            loadData(BIRTHDAY_KEY, Type.BIRTHDAY)
            loadData(CARD_NUMBER_KEY, Type.CARD_NUMBER)
            loadData(RENEWAL_KEY, Type.RENEWAL)
            loadData(ID_NUMBER_KEY, Type.ID_NUMBER)
            loadData(PLACE_KEY, Type.PLACE_OF_ISSUE)
            loadData(SEX_KEY, Type.SEX)
            loadData(DATE_KEY, Type.DATE_OF_ISSUE)
            loadData(ADDRESS_KEY, Type.ADDRESS)
        }
    }

    // 데이터 저장
    suspend fun saveData(
        key: Preferences.Key<String>,
        value: String,
    ) {
        application.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun loadData(
        key: Preferences.Key<String>,
        type: Type
    ) {
        try {
            val result = application.dataStore.data
                .map { preferences ->
                    preferences[key] ?: ""
                }.first()
            when (type) {
                Type.IMAGE -> profile.emit(result)
                Type.NICKNAME -> nickname.emit(result)
                Type.BIRTHDAY -> birthday.emit(result)
                Type.RENEWAL -> renewal.emit(result)
                Type.ID_NUMBER -> idNumber.emit(result)
                Type.PLACE_OF_ISSUE -> placeOfIssue.emit(result)
                Type.SEX -> sex.emit(result)
                Type.CARD_NUMBER -> cardNumber.emit(result)
                Type.DATE_OF_ISSUE -> dateOfIssue.emit(result)
                Type.ADDRESS -> address.emit(result)
            }
            _successLoadData.emit(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

enum class Type {
    IMAGE, NICKNAME, BIRTHDAY, RENEWAL, ID_NUMBER, PLACE_OF_ISSUE, SEX, CARD_NUMBER, DATE_OF_ISSUE, ADDRESS
}
