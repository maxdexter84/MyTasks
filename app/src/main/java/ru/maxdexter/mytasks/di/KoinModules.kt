package ru.maxdexter.mytasks.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.maxdexter.mytasks.data.firebase.IDataStorage
import ru.maxdexter.mytasks.data.localdatabase.ILocalDatabase
import ru.maxdexter.mytasks.data.localdatabase.LocalDatabaseImpl
import ru.maxdexter.mytasks.data.firebase.IRemoteDatabase
import ru.maxdexter.mytasks.data.firebase.RemoteDatabaseImpl
import ru.maxdexter.mytasks.data.firebase.StorageImplI
import ru.maxdexter.mytasks.data.localdatabase.RoomDb
import ru.maxdexter.mytasks.domen.common.IRepository
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.repository.RepositoryImpl
import ru.maxdexter.mytasks.ui.activity.MainViewModel
import ru.maxdexter.mytasks.ui.entity.UITask
import ru.maxdexter.mytasks.ui.fragments.calendar.CalendarViewModel
import ru.maxdexter.mytasks.ui.fragments.newtask.NewTaskViewModel
import ru.maxdexter.mytasks.ui.fragments.notifications.NotificationsViewModel
import ru.maxdexter.mytasks.ui.fragments.profile.ProfileViewModel



@OptIn(KoinApiExtension::class)
@ExperimentalCoroutinesApi
val application = module {
    single(named("Dao")) { Room.databaseBuilder(get(),
        RoomDb::class.java,
        "app_db.db").build().getDao() }
    single(named("firestore")){Firebase.firestore}
    single(named("uiTask")){UITask()}
    single(named("firebaseAuth")) { FirebaseAuth.getInstance()}
    single<ILocalDatabase>(named("localDatabase")) { LocalDatabaseImpl(get(named("Dao"))) }
    single<IRemoteDatabase>(named("remoteDatabase")) { RemoteDatabaseImpl(get(named("firestore")),get(named("firebaseAuth"))) }
    single<IRepository>(named("repository")) { RepositoryImpl(get(named("localDatabase")), get(named("remoteDatabase"))) }
    single(named("storageInstance")){FirebaseStorage.getInstance()}
    single (named("appPref")) { AppPreferences(get()) }
    single<IDataStorage> (named("dataStorage")){ StorageImplI(get(named("storageInstance")),get())  }


}

@OptIn(InternalCoroutinesApi::class)
val newTaskModule = module {
        viewModel {(uuid: String) -> NewTaskViewModel(uuid,get(named("uiTask")),get(named("repository")),get())}

}

val calendarModule = module {
    viewModel { CalendarViewModel(get(named("repository"))) }
}


val notificationModule = module {
    viewModel { NotificationsViewModel() }
}

val profileModule = module {
    viewModel { ProfileViewModel(get(named("appPref"))) }
}
val mainViewModel = module {
    viewModel { MainViewModel(get(named("repository")),get(named("dataStorage"))) }
}