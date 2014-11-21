import os
import shutil
from datetime import datetime
import platform
import tarfile

def make_tarfile(output_filename, source_dir):
    name = source_dir.split('/')
    tar = tarfile.open(output_filename + ".tar.gz", "w:gz")
    tar.add(source_dir, arcname=name[-1])
    tar.close()

def compress(path):
	i = datetime.now()
	time=i.strftime('%Y|%m|%d-%H:%M:%S')
	os.mkdir(path + "/" + time)
	os.mkdir(path + "/" + "be-amplify")
	os.mkdir(path + "/wav")
	os.mkdir(path + "/corrections")
	lines = [line.strip() for line in open(path + '/bkp/utf-8.txt')]
	count = 1
	for i in lines:
	    name = i.split(')')
	    source = path + "/bkp/" + str(count) + ".wav"
	    destination = path + "/be-amplify/" + name[0] + ".wav"
	    shutil.copy(source, destination)
	    shutil.copy(destination, path + "/wav")
	    count = count + 1

	if(platform.system() == "Linux"):
		os.system("./amplify.sh " + path + "/wav " + path + "/wav")
	shutil.copy(path + "/bkp/utf-8.txt", path + "/be-amplify")
	shutil.copytree(path + "/wav", path + "/" + time + "/wav");
	shutil.copytree(path + "/wav",path + "/corrections/wav")
	shutil.copy(path + "/bkp/utf-8.txt", path + "/" + time)
	shutil.copy(path + "/bkp/utf-8.txt", path + "/corrections")
	shutil.copy(path + "/bkp/utf-8.txt", path + "/be-amplify")
	shutil.copy(path + "/bkp/itrans.txt", path + "/corrections")
	make_tarfile(path + "/" + time, path + "/" + time)
	make_tarfile(path + "/corrections", path + "/corrections")

