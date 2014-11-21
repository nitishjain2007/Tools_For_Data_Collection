from PyQt4 import QtGui

class About(QtGui.QDialog):

	def __init__(self):
		super(About, self).__init__()
		self.initUI()

	def initUI(self):

		self.pic = QtGui.QLabel(self)
		self.pic.setGeometry(20, 25, 200, 100)
		self.pic.setPixmap(QtGui.QPixmap('logo.png'))

		self.text = QtGui.QLabel(self)
		self.text.setGeometry(210, 20, 250, 100)
		self.text.setStyleSheet("QLabel {font-size: 15pt}")
		self.text.setText('Voice Recorder Application')

		self.setGeometry(300,280, 500, 150)
		self.setFixedSize(500 , 150)
		self.setWindowTitle('About')
		self.setWindowIcon(QtGui.QIcon('logo.png'))
		self.show()