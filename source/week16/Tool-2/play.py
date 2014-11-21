import pyaudio
import wave
import time
from PyQt4 import QtGui
from PyQt4.QtCore  import *

class Play(QObject):

	def __init__(self):
		super(Play, self).__init__()
		self.rec=False

	def play(self, path, name, Gui):
		self.rec=True
		CHUNK = 15
		out = str(path+"/"+name+".wav")
		wf = wave.open(out, 'rb')

		p = pyaudio.PyAudio()

		stream = p.open(format=p.get_format_from_width(wf.getsampwidth()), channels=wf.getnchannels(), rate=wf.getframerate(), output=True)

		data = wf.readframes(CHUNK)

		while data != '' and self.rec == True:
			stream.write(data)
			data = wf.readframes(CHUNK)
			QCoreApplication.processEvents()

		stream.stop_stream()
		stream.close()

		p.terminate()
		Gui.Record.setEnabled(True)
		Gui.Open.setEnabled(True)
		Gui.Play.setEnabled(True)
		Gui.Next.setEnabled(True)
		Gui.Back.setEnabled(True)
		Gui.statusBar().showMessage('')

	def stop(self):
		self.rec=False