' @file   clipboard-quotes.vbs
' @Author Frank (Frank@CrookedThumbSoftware.com)
' @date   November 2012
' @brief  Will read the default (or specified) quote file, select a random line, and copy it to the clipboard.
' Copyright (c) 2012, Crooked Thumb Software, All rights reserved.
Option Explicit

Dim objFSO, quoteFile, allLines, strLine, quoteArray, line, WshShell, oExec, oIn
CONST ForReading = 1

'name of the text file
quoteFile = "../Quotes.txt"

'If there's one command argument
If Wscript.Arguments.Count = 1 Then
	'Use the command argument as the quote file
	quoteFile = Wscript.Arguments(0)
Else
	'If the wrong number of command arguments was provided
	If Wscript.Arguments.Count > 1 Then
		MsgBox("Usage is clipboard-quotes.vbs <optional path to quote file>")
		'Abort the script
		WScript.Quit
	End If
End If

'Create a File System Object
Set objFSO = CreateObject("Scripting.FileSystemObject")

'Open the quote file and read all of it
allLines = objFSO.OpenTextFile(quoteFile,ForReading).ReadAll

'Split the string into lines
quoteArray = Split(allLines,vbCrLf)

'Grab a random line
Randomize
do
	'Get a random number in the range [0,size of quoteArray)
	line = Int((UBound(quoteArray)+1)*Rnd)
'double-check that the line is valid
loop while quoteArray(line) = Empty
	
'Copy the line to the clipboard using the commnad line tool "clip.exe"
Set WshShell = CreateObject("WScript.Shell")
Set oExec = WshShell.Exec("clip")
Set oIn = oExec.stdIn
'Wrap the quote with double-quotes, chr(34) = "
oIn.Write chr(34) + quoteArray(line) + chr(34)
oIn.Close

' loop until it's done
Do While oExec.Status = 0
    WScript.Sleep 100
Loop

'Clean up
Set oIn = Nothing
Set oExec = Nothing
Set objFSO = Nothing
