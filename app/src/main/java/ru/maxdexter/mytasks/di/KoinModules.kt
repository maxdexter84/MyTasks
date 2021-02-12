package ru.maxdexter.mytasks.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.maxdexter.mytasks.domen.repository.DataStorage
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.localdatabase.LocalDatabaseImpl
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.domen.repository.firebase.RemoteDataProviderImpl
import ru.maxdexter.mytasks.domen.repository.firebase.StorageImpl
import ru.maxdexter.mytasks.domen.repository.localdatabase.RoomDb
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.ui.MainViewModel
import ru.maxdexter.mytasks.ui.calendar.CalendarViewModel
import ru.maxdexter.mytasks.ui.newtask.NewTaskViewModel
import ru.maxdexter.mytasks.ui.notifications.NotificationsViewModel
import ru.maxdexter.mytasks.ui.profile.ProfileViewModel


@OptIn(KoinApiExtension::class)
val application = module {
    single(named("room")) { Room.databaseBuilder(get(),
        RoomDb::class.java,
        "app_db.db").build() }
    single(named("firestore")){Firebase.firestore}
    single(named("firebaseAuth")) { FirebaseAuth.getInstance()}
    single(named("storageInstance")){FirebaseStorage.getInstance()}
    single (named("appPref")) { AppPreferences(get()) }
    single<DataStorage> (named("dataStorage")){ StorageImpl(get(named("storageInstance")),get())  }
    single<RemoteDataProvider>(named("fireStoreProvider")) { RemoteDataProviderImpl(get(named("firestore")),get(named("firebaseAuth"))) }
    single<LocalDatabase>(named("repository")) { LocalDatabaseImpl(get(named("room"))) }
}

val newTaskModule = module {
        viewModel {(uuid: String) -> NewTaskViewModel(uuid,get(named("repository")),get(named("fireStoreProvider")),get(
            named("dataStorage")),get()) }

}

val calendarModule = module {
    viewModel { CalendarViewModel(get(named("repository")), get(named("fireStoreProvider"))) }
}


val notificationModule = module {
    viewModel { NotificationsViewModel() }
}

val profileModule = module {
    viewModel { ProfileViewModel(get(named("appPref"))) }
}
val mainViewModel = module {
    viewModel { MainViewModel(get(named("fireStoreProvider"))) }
}