if [ $# != 2 ]; then
	echo "Usage: sh amplify.sh <input folder> <output folder>"
	exit
fi

indir=$1
outdir=$2

if [ ! -d $outdir ]; then
	mkdir $outdir
fi

#set path='/usr/local/SPTK/bin/'
#export path

ls $indir/*.wav | while read i
do
	tmpfile=tmp_$$
	fname=`basename $i .wav`
	echo "Processing $fname"
	sox $i $tmpfile.raw
	amp=`/home5/speech/installations/SPTK-3.5/bin/x2x/x2x +sf $tmpfile.raw | /home5/speech/installations/SPTK-3.5/bin/minmax/minmax | /home5/speech/installations/SPTK-3.5/bin/sopr/sopr -ABS | /home5/speech/installations/SPTK-3.5/bin/minmax/minmax | /home5/speech/installations/SPTK-3.5/bin/x2x/x2x +fa | tail -1`
	if [ $amp -le 7500 ]; then
		sox -v 2.4 $i $outdir/$fname.wav
	elif [ $amp -le 8000 ]; then
		sox -v 2.2 $i $outdir/$fname.wav
	elif [ $amp -le 9000 ]; then
		sox -v 2 $i $outdir/$fname.wav
	elif [ $amp -le 10000 ]; then
		sox -v 1.8 $i $outdir/$fname.wav
	elif [ $amp -le 11000 ]; then
		sox -v 1.6 $i $outdir/$fname.wav
	elif [ $amp -le 16000 ]; then
		sox -v 1.5 $i $outdir/$fname.wav
	else
		sox $i -r 48000 $outdir/$fname.wav
	fi
	rm $tmpfile.raw
	#break;
done

