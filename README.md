Добавлен новый класс HistoryController.
В классе ChatController в метод  openConnection() добавлен код:
HistoryController fileLog = new HistoryController(login);
chatArea.appendText(strFromServer + "\n");
fileLog.saveMsg(strFromServer + "\n");

Не могу понять, как получить значение Login в данном классе((

Со вторым заданием разобраться не получилось
