package ru.maxdexter.mytasks.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.maxdexter.mytasks.domen.repository.LocalDatabase
import ru.maxdexter.mytasks.domen.repository.LocalDatabaseImpl
import ru.maxdexter.mytasks.domen.repository.RemoteDataProvider
import ru.maxdexter.mytasks.domen.repository.firebase.FireStoreProvider
import ru.maxdexter.mytasks.domen.repository.localdatabase.RoomDb
import ru.maxdexter.mytasks.preferences.AppPreferences
import ru.maxdexter.mytasks.ui.calendar.CalendarViewModel
import ru.maxdexter.mytasks.ui.detail.DetailViewModel
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
    single (named("appPref")) { AppPreferences(get()) }
    single<RemoteDataProvider>(named("FireStoreProvider")) { FireStoreProvider(get(named("firestore")),get(named("firebaseAuth"))) }
    single<LocalDatabase>(named("repository")) { LocalDatabaseImpl(get(named("room")))}
}

val newTaskModule = module {
        viewModel { NewTaskViewModel(get(named("repository")),get()) }

}

val calendarModule = module {
    viewModel { CalendarViewModel(get(named("repository"))) }
}

val detailModule = module {
    viewModel {(uuid: String?) -> DetailViewModel(uuid, get(named("repository"))) }
}

val notificationModule = module {
    viewModel { NotificationsViewModel() }
}

val profileModule = module {
    viewModel { ProfileViewModel(get(named("appPref"))) }
}