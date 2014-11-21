from PyQt4 import QtGui
from PyQt4.QtCore  import QThread
from about import About
from rec import VoiceRec
from play import Play
from user import User
import sys, os
from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.backends.backend_qt4agg import NavigationToolbar2QTAgg as NavigationToolbar
import matplotlib.pyplot as plt

class Gui(QtGui.QMainWindow,QtGui.QWidget):

	def __init__(self):
		super(Gui, self).__init__()
		self.speaker = ""
		self.directory = ""
		self.type = ""
		self.line=0
		self.text=[]
		self.decode=""
		self.fname="output"
		self.rec=0
		self.initUI()
		self.file = ""

	def initUI(self):
		self.setWindowIcon(QtGui.QIcon('logo.png'))
		self.vr = VoiceRec()
		self.pl = Play()
		self.vrThread = QThread()
		self.plThread = QThread()
		self.vr.moveToThread(self.vrThread)
		self.pl.moveToThread(self.plThread)

		self.figure = plt.figure(1)
		self.canvas = FigureCanvas(self.figure)
		self.toolbar = NavigationToolbar(self.canvas, self)
		self.centralwidget = QtGui.QWidget(self)
		self.verticalLayout = QtGui.QVBoxLayout(self.centralwidget)
		self.verticalLayout.addWidget(self.toolbar)
		self.verticalLayout.addWidget(self.canvas)
		self.centralwidget.setGeometry(10,10,825,330)

		openFile = QtGui.QAction(QtGui.QIcon('open.png'), '&Open', self)
		openFile.setShortcut('Ctrl+O')
		openFile.setStatusTip('Open new File')
		openFile.triggered.connect(self.showDialogOpen)

		chUser = QtGui.QAction('&Change Speaker', self)
		chUser.setStatusTip('Change Speaker')
		chUser.triggered.connect(self.changeUser)

		exitAction = QtGui.QAction(QtGui.QIcon('exit.png'), '&Exit', self)
		exitAction.setShortcut('Ctrl+Q')
		exitAction.setStatusTip('Exit application')
		exitAction.triggered.connect(self.closeEvent)

		utf_8 = QtGui.QAction('&UTF-8', self)
		utf_8.setStatusTip('utf-8')
		utf_8.triggered.connect(self.encodeUTF8)

		utf_16 = QtGui.QAction('&UTF-16', self)
		utf_16.setStatusTip('utf-16')
		utf_16.triggered.connect(self.encodeUTF16)

		Ascii = QtGui.QAction('&ASCII', self)
		Ascii.setStatusTip('ascii')
		Ascii.triggered.connect(self.encodeASCII)

		about = QtGui.QAction('&About', self)
		about.setStatusTip('More About Voice Recorder')
		about.triggered.connect(self.showAbout)

		self.textEdit = QtGui.QTextEdit(self)
		self.textEdit.setGeometry(10,360,825,104)
		self.textEdit.setStyleSheet("QTextEdit {font-size: 14pt}")
		self.textEdit.setText("Please Open a File...")
		self.textEdit.setReadOnly(True)

		self.Open = QtGui.QPushButton('Open', self)
		self.Open.move(10,480)
		self.Open.clicked.connect(self.showDialogOpen)

		self.Record = QtGui.QPushButton('Record', self)
		self.Record.move(155,480)
		self.Record.setEnabled(False)
		self.Record.clicked.connect(self.record)

		self.Stop = QtGui.QPushButton('Stop', self)
		self.Stop.move(300,480)
		self.Stop.setEnabled(False)
		self.Stop.clicked.connect(self.stop)

		self.Play = QtGui.QPushButton('Play', self)
		self.Play.move(445,480)
		self.Play.setEnabled(False)
		self.Play.clicked.connect(self.play)

		self.Back = QtGui.QPushButton('Back', self)
		self.Back.move(590,480)
		self.Back.setEnabled(False)
		self.Back.clicked.connect(self.showBack)

		self.Next = QtGui.QPushButton('Next', self)
		self.Next.move(735,480)
		self.Next.setEnabled(False)
		self.Next.clicked.connect(self.showNext)

		menubar = self.menuBar()
		fileMenu = menubar.addMenu('&File')
		fileMenu.addAction(openFile)
		fileMenu.addAction(chUser)
		fileMenu.addAction(exitAction)
		encodeMenu = menubar.addMenu('&Encoding')
		encodeMenu.addAction(Ascii)
		encodeMenu.addAction(utf_8)
		encodeMenu.addAction(utf_16)
		helpMenu = menubar.addMenu('&Help')
		helpMenu.addAction(about)

		self.setGeometry(200,200, 845, 550)
		self.setFixedSize(845 , 550)
		self.setWindowTitle('Akshar Voice Recorder')
		self.user = User(self)

	def showDialogOpen(self):
		plt.clf()
		self.canvas.draw()
		self.statusBar().showMessage('Open a File')
		self.fname = QtGui.QFileDialog.getOpenFileName(self, 'Open file')
		if(self.fname!=""):
			self.Record.setEnabled(True)
			self.Play.setEnabled(True)
			self.Next.setEnabled(True)
			self.Back.setEnabled(True)
			self.directory=str(self.file)+"/"+str(self.speaker)+"_"+str(self.type)+"_"+str(self.fname).split("/")[-1]
			if not os.path.exists(self.directory):
				os.makedirs(self.directory)
			del self.text[:]
			f = open(self.fname, 'r')
			for lines in f:
				self.text.append(lines)
			f.close
			if(self.decode!=""):
				self.textEdit.setText(self.text[self.line].decode(self.decode))
			else:
				self.textEdit.setText(self.text[self.line].decode('ascii'))
			self.line=0
		else:
			self.Record.setEnabled(False)
			self.Play.setEnabled(False)
			self.Next.setEnabled(False)
			self.Back.setEnabled(False)
		self.statusBar().showMessage('')

	def showNext(self):
		plt.clf()
		self.canvas.draw()
		self.line+=1
		if(len(self.text)>self.line):
			if(self.decode!=""):
				self.textEdit.setText(self.text[self.line].decode(self.decode))
			else:
				self.textEdit.setText(self.text[self.line].decode('utf-8'))
		else:
			self.showDialogOpen()

	def showBack(self):
		plt.clf()
		self.canvas.draw()
		self.line-=1
		if(len(self.text)>=self.line and self.line>=0):
			if(self.decode!=""):
				self.textEdit.setText(self.text[self.line].decode(self.decode))
			else:
				self.textEdit.setText(self.text[self.line].decode('utf-8'))
		else:
			self.showDialogOpen()

	def showAbout(self):
		self.popup1=About()
		self.popup1.exec_()

	def encodeUTF8(self):
		self.decode="utf-8"

	def encodeUTF16(self):
		self.decode="utf-16"

	def encodeASCII(self):
		self.decode="ascii"

	def changeUser(self):
		self.user.__init__(self)

	def record(self):
		plt.clf()
		self.canvas.draw()
		self.statusBar().showMessage('Recording')
		self.rec=1
		self.Record.setEnabled(False)
		self.Stop.setEnabled(True)
		self.Open.setEnabled(False)
		self.Play.setEnabled(False)
		self.Next.setEnabled(False)
		self.Back.setEnabled(False)
		self.vrThread.start()
		self.vr.record(self.directory, self.text[self.line].split(")")[0], self)

	def stop(self):
		self.statusBar().showMessage('')
		if self.rec == 1:
			self.vr.stop()
			self.vrThread.exit()
			self.vrThread.wait()
			self.vrThread.quit()
		elif self.rec == 2:
			self.pl.stop()
			self.plThread.exit()
			self.plThread.wait()
			self.plThread.quit()
		self.Record.setEnabled(True)
		self.Open.setEnabled(True)
		self.Play.setEnabled(True)
		self.Next.setEnabled(True)
		self.Back.setEnabled(True)
		self.rec=0

	def play(self):
		self.statusBar().showMessage('Playing')
		self.rec=2
		self.Record.setEnabled(False)
		self.Stop.setEnabled(True)
		self.Open.setEnabled(False)
		self.Play.setEnabled(False)
		self.Next.setEnabled(False)
		self.Back.setEnabled(False)
		self.plThread.start()
		self.pl.play(self.directory, self.text[self.line].split(")")[0], self)
