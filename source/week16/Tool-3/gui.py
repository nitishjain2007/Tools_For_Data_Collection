from PyQt4 import QtGui
from compress import *

class Gui(QtGui.QMainWindow,QtGui.QWidget):

	def __init__(self):
		super(Gui, self).__init__()
		self.file=""
		self.initUI()

	def initUI(self):

		self.dest = QtGui.QLabel(self)
		self.dest.setGeometry(10, 18, 100, 20)
		self.dest.setText('Destination')

		self.but = QtGui.QPushButton(self)
		self.but.setGeometry(120, 17, 100, 25)
		self.but.setText('Browse')
		self.but.clicked.connect(self.browse)

		self.ok = QtGui.QPushButton(self)
		self.ok.setGeometry(80, 70, 100, 25)
		self.ok.setText('Execute')
		self.ok.clicked.connect(self.gui)

		self.but2 = QtGui.QPushButton(self)
		self.but2.setGeometry(190, 70, 100, 25)
		self.but2.setText('Close')
		self.but2.clicked.connect(self.close)

		self.setGeometry(300,280, 300, 100)
		self.setFixedSize(300, 100)
		self.setWindowTitle('ITrans')
		self.setWindowIcon(QtGui.QIcon('logo.png'))
		self.show()

	def browse(self):
		self.file = str(QtGui.QFileDialog.getExistingDirectory(self, "Select Directory"))

	def gui(self):
		if self.file == "":
			QtGui.QMessageBox.about(self, "Error", "Please Select Destination Directory")
		else:
			compress(self.file)
			self.close()
