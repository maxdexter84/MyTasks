package ru.maxdexter.mytasks.domen.extension

import ru.maxdexter.mytasks.data.localdatabase.entity.Task
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskFile
import ru.maxdexter.mytasks.data.localdatabase.entity.TaskWithTaskFile
import ru.maxdexter.mytasks.ui.entity.UIFile
import ru.maxdexter.mytasks.ui.entity.UITask


fun TaskWithTaskFile.mapToUITask():UITask{
    return UITask(task.id,
       title = task.title,
       description = task.description,
       pushMessage = task.pushMessage,
       eventYear =  task.eventYear,
        eventMonth = task.eventMonth,
       eventDay =  task.eventDay,
       eventHour =  task.eventHour,
        eventMinute = task.eventMinute,
       repeat =  task.repeat,
       repeatRangeValue =  task.repeatRangeValue,
        fileList = this.list.map { it.mapToUIFile() },
        userNumber = task.userNumber
    )
}

fun TaskFile.mapToUIFile():UIFile{
    return UIFile(uri = this.uri,
        saveToCloud = this.saveToCloud,
        taskUUID = this.taskUUID,
        fileType = this.fileType,
        name = this.name)
}