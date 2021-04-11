; #NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
#Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.

SetWorkingDir C:\Projekte\se-carrental\CloudDeployment

^+i::Run,C:\Projekte\se-carrental\CloudDeployment\apply.cmd   ; Applies Terraform instances with Ctrl+Shift+i
^+o::Run,C:\Projekte\se-carrental\CloudDeployment\destroy.cmd ; Destroy Terraform instances with Ctrl+Shift+o