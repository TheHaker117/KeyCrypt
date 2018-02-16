## KeyCrypt


KeyCrypt is a keylogger that uses the Cryptstack Algorithm for working. The basics concepts of its working are:
- At the first time that run, create a hidden folder called ".datasystem".
- Starts a keylistener using JNativeHook library.
- Captures everything that you type in the keyboard
- All data is saved in a text file that includes the date of each line that you wrote.
- After do that, encrypts the file and renamed to KC-XXXX.kc (XXXX are the creation date of the file).
- Then, every so often sends the data to a online server.
- While is working, KeyCrypt is encrypting and decrypting the data file.
- Every day creates a new file.
	
This project contains both the documentation in JavaDoc format and the UML diagrams.  
And of course, a executable JAR too.  


**Note 1:** *Full Crypstack Algorithm is not available yet*  
**Note 2:** *KeyCrypt was a school project, its purpose its only educative*  
**Note 3:** *I'm not the owner of the libraries that I used for develop KeyCrypt, please check JNativeHook on GitHub*
