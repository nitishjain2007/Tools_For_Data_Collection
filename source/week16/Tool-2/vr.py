import pyaudio
import wave
import sys
import time
from PyQt4 import QtGui
from PyQt4.QtCore  import *
from gui import Gui
from about import About
from rec import VoiceRec
from play import Play

def main():
	app = QtGui.QApplication(sys.argv)
	gui = Gui()
	sys.exit(app.exec_())

if __name__ == '__main__':
	main()