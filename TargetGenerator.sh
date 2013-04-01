#!/bin/sh 
#generates project target files
#in desktop
AppJar='FLLTutorial.jar'

JarFolder='FLLTutorial/target/'
JarFileName='FLLTutorial-1.0-SNAPSHOT.jar'
JarFile=$JarFolder$JarFileName
ManifestDir='META-INF/'
ManifestFile=$ManifestDir'MANIFEST.MF'
ManifestFileName=${ManifestFile##*/}
MainClass='com.honeybadgers.flltutorial.App'


#obtain the manifest file
echo "->	obtaining manifest file"
unzip -jo $JarFile $ManifestFile 1&> /dev/null
#zip -d  ${JarFolder}${JarFileName} ${ManifestFile}

#cat $ManifestFileName
#add main class attribute to manifest, if it does not already
#have that attribute
if [[ `grep "Main-Class" $ManifestFileName | wc -l | bc` -gt 0 ]];
then
	echo "->	manifest file already has Main-Class attribute"
else
	#create new manifest file
	#first, clean up empty lines, as the manifest sometimes
	mkdir $ManifestDir
	grep -v '^\r$' $ManifestFileName > $ManifestFile
	echo "Main-Class: ${MainClass}" >> $ManifestFile

	echo "->	Main-Class attribute inserted into manifest file"
	#cat $ManifestFile
	#include file in manifest of targt jar
	echo "->	manifest file updated"
	zip $JarFile $ManifestFile 1&> /dev/null
fi
rm -rf $ManifestDir $ManifestFileName

#set -xv

#copy pertinent folders and files to the following directory
TargetFolder=~/Desktop/FLLTutorialRelease/
ResourcesFolder=src/main/resources/
TargetResources=$TargetFolder$ResourcesFolder

echo "->	generating release files"

if [[ ! -e $TargetFolder ]];
then
	mkdir -p $TargetResources
else
	rm -r ${TargetFolder}*
	mkdir -p $TargetResources
fi

cp $JarFile $TargetFolder
cp -r FLLTutorial/${ResourcesFolder}* $TargetResources

#get all needed jars
echo "-> 	copying needed jars"

find ~/ -name swing-layout*.jar -exec cp '{}' $TargetFolder \; 2> /dev/null
SwingLayoutJar=`(cd $TargetFolder; ls swing-layout*.jar)`;

echo "-> 	copied [$SwingLayoutJar]"

#combining jars
echo "-> 	combining jars"

cd $TargetFolder
rm -rf tmp/
mkdir tmp/

#extract each jar to /tmp
cd tmp/
jar -xf ../$SwingLayoutJar
jar -xf ../$JarFileName 
cd ../
#clean up jars
rm -rf *.jar

jar -cfm $AppJar tmp/META-INF/MANIFEST.MF -C tmp/ .

#clean up tmp
rm -rf tmp/
echo "->	release generation finished"
