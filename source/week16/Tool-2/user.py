from PyQt4 import QtGui

class User(QtGui.QMainWindow,QtGui.QWidget):

	def __init__(self, Gui):
		super(User, self).__init__()
		self.file=""
		self.x = Gui
		self.initUI()

	def initUI(self):

		self.name = QtGui.QLabel(self)
		self.name.setGeometry(10, 14, 100, 20)
		self.name.setText('Speaker Name')

		self.text = QtGui.QLineEdit(self)
		self.text.setGeometry(120, 10, 200, 25)
		self.text.setText('FirstName')

		self.type = QtGui.QLabel(self)
		self.type.setGeometry(75, 41, 100, 20)
		self.type.setText('Type')

		self.dropdown = QtGui.QComboBox(self)
		self.dropdown.setGeometry(120, 40, 100, 25)
		self.dropdown.insertItem(1,'Select')
		self.dropdown.insertItem(2,'Close')
		self.dropdown.insertItem(3,'Distant')

		self.dest = QtGui.QLabel(self)
		self.dest.setGeometry(10, 68, 100, 20)
		self.dest.setText('Destination')

		self.but = QtGui.QPushButton(self)
		self.but.setGeometry(120, 71, 100, 25)
		self.but.setText('Browse')
		self.but.clicked.connect(self.browse)

		self.but = QtGui.QPushButton(self)
		self.but.setGeometry(120, 70, 100, 25)
		self.but.setText('Browse')
		self.but.clicked.connect(self.browse)

		self.ok = QtGui.QPushButton(self)
		self.ok.setGeometry(400, 120, 50, 25)
		self.ok.setText('OK')
		self.ok.clicked.connect(self.gui)

		self.setGeometry(300,280, 500, 150)
		self.setFixedSize(500 , 150)
		self.setWindowTitle('Profile')
		self.setWindowIcon(QtGui.QIcon('logo.png'))
		self.show()

	def browse(self):
		self.x.file = str(QtGui.QFileDialog.getExistingDirectory(self, "Select Directory"))

	def gui(self):
		if self.x.file == "":
			QtGui.QMessageBox.about(self, "Error", "Please Select Destination Directory")
		elif self.text.text() == "FirstName":
			QtGui.QMessageBox.about(self, "Error", "Please write Name")
		elif str(self.dropdown.currentText()) == "Select":
			QtGui.QMessageBox.about(self, "Error", "Please select Type")
		else:
			self.close()
			self.x.speaker = self.text.text()
			self.x.type = str(self.dropdown.currentText())
			self.x.show()
