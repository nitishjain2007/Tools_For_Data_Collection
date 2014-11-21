import pyaudio
import wave
import time
from PyQt4 import QtGui
from PyQt4.QtCore  import *
import os
from scipy.io import wavfile
from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.backends.backend_qt4agg import NavigationToolbar2QTAgg as NavigationToolbar
import matplotlib.pyplot as plt
import numpy as np
import struct
import contextlib

class VoiceRec(QObject):

	def __init__(self):
		"""Initializer"""
		super(VoiceRec, self).__init__()
		self.rec = False

	def record(self, path, name, Gui):
		"""Starts Recording"""
		self.rec=True
		CHUNK = 1024
		FORMAT = pyaudio.paInt16
		CHANNELS = 1
		RATE = 48000
		out = path+"/"+name+".wav"
		WAVE_OUTPUT_FILENAME = str(out)

		p = pyaudio.PyAudio()

		stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, frames_per_buffer=CHUNK)

		print("* recording")
		frames = []
		while 1:
			data = stream.read(CHUNK)
			frames.append(data)
			QCoreApplication.processEvents()
			if self.rec == False:
				break

		print("* done recording")

		stream.stop_stream()
		stream.close()
		p.terminate()

		wf = wave.open(WAVE_OUTPUT_FILENAME, 'wb')
		wf.setnchannels(CHANNELS)
		wf.setsampwidth(p.get_sample_size(FORMAT))
		wf.setframerate(RATE)
		wf.writeframes(b''.join(frames))
		wf.close()
		self.noisered(out)
		self.plot(out, Gui)
		if self.checkendsilence(WAVE_OUTPUT_FILENAME) == False:
			QtGui.QMessageBox.about(Gui, "Error", "Please Record Again")
			Gui.Next.setEnabled(False)
			Gui.Back.setEnabled(False)
			Gui.Open.setEnabled(False)
		else:
			Gui.Next.setEnabled(True)
			Gui.Back.setEnabled(True)
			Gui.Open.setEnabled(True)
		if self.checkfrequency(WAVE_OUTPUT_FILENAME) > 32000:
			QtGui.QMessageBox.about(Gui, "Error", "Please Record Again")
			Gui.Next.setEnabled(False)
			Gui.Back.setEnabled(False)
			Gui.Open.setEnabled(False)
		else:
			Gui.Next.setEnabled(True)
			Gui.Back.setEnabled(True)
			Gui.Open.setEnabled(True)
		
	def stop(self):
		"""Stops Recording"""
		self.rec=False

	def plot(self, name, Gui):
		"""Plot the graph of wav file"""
		spf = wave.open(name,'r')

		#Extract Raw Audio from Wav File
		signal = spf.readframes(-1)
		signal = np.fromstring(signal, 'Int16')
		fs = spf.getframerate()

		#If Stereo
		if spf.getnchannels() == 2:
		    print 'Just mono files'
		    sys.exit(0)

		Time=np.linspace(0, len(signal)/fs, num=len(signal))
		plt.clf()
		Gui.ax=Gui.figure.add_subplot(111)
		Gui.ax.plot(Time,signal)
		Gui.canvas.draw()
		

	def noisered(self, name):
		"""Noise Reduction"""
		inputgiven=name
		output = inputgiven.split('.')[0] + 'filtered.wav'
		os.system("sox " + inputgiven + " -n trim 0 0.5 noiseprof myprofile")
		os.system("sox " + inputgiven + " " + output + " noisered myprofile 0.2")
		os.system("sox " + output + " -c 1 " + inputgiven)
		os.remove(output)
		os.remove('myprofile')
		
	def getlastslice(self, inputtaken):
		""" This function takes the last slice from a input wav file"""
		output = inputtaken.split('.')[0] + 'last.wav'
		with contextlib.closing(wave.open(inputtaken, 'r')) as framesget:
			frames = framesget.getnframes()
			rate = framesget.getframerate()
			duration = frames / float(rate)#duration of the input file
		duration1 = duration-0.1
		win = wave.open(inputtaken, 'rb')
		wout = wave.open(output, 'wb')
		time0, time1 = duration, duration1 # get audio for the last 100 miliseconds
		start0 = int(time1 * win.getframerate())
		start1 = int(time0 * win.getframerate())
		win.readframes(start0)
		frames = win.readframes(start1-start0)
		wout.setparams(win.getparams())
		wout.writeframes(frames)
		win.close()
		wout.close()
		return output

	def checkendsilence(self, inputgiven):
		""" This function checks for the endsilence of an input wav file"""
		output = self.getlastslice(inputgiven)
		wave_file = wave.open(output, "r")
		for i in range(wave_file.getnframes()):
			current_frame = wave_file.readframes(1)
			unpacked_signed_value = struct.unpack("<h", current_frame)
			if abs(unpacked_signed_value[0]) > 500:
				return False
		return True

	def checkfrequency(self, inputgiven):
		""" This function returns the maximum frequency of a wav file"""
		data_size = 40000
		wav_file = wave.open(inputgiven, 'r')
		data = wav_file.readframes(data_size)
		wav_file.close()
		data = struct.unpack('{n}h'.format(n=data_size), data)
		#print(len(data))
		#print max(data)
		data = np.array(data)
		frequencylist = np.fft.fft(data)
		frequencylistactual = 2*abs(frequencylist)/frequencylist.size
		return max(frequencylistactual)
