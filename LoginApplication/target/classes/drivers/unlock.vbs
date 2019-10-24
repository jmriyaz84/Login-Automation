Set WshShell = WScript.CreateObject("WScript.Shell")
Do
	WScript.Sleep 5000
	WshShell.SendKeys "{NUMLOCK}"
	i = 0
Loop Until (i=1)

