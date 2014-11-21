from PyQt4 import QtGui
from PyQt4.QtCore  import *
from gui import Gui
import sys

def main():
	app = QtGui.QApplication(sys.argv)
	gui = Gui()
	sys.exit(app.exec_())

if __name__ == '__main__':
	main()