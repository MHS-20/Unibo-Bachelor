#defineSIGHUP 1        Hangup(POSIX). Action: exit 
#defineSIGINT 2        Interrupt (ANSI). Action: exit  (^C)
#defineSIGQUIT 3      Quit(POSIX). Action: exit, core dump
#defineSIGILL  4      Illegalinstr.(ANSI).Action: exit,coredump
#defineSIGKILL 9      Kill, unblockable(POSIX). Action: exit
#defineSIGUSR1 10      User-definedsignal1 (POSIX). Action: exit
#defineSIGSEGV 11      Segm. violation(ANSI). Act: exit,coredump 
#defineSIGUSR2 12      User-definedsignal2 (POSIX).Act: exit 
#defineSIGPIPE 13      Brokenpipe (POSIX).Act: exit  
#defineSIGALRM 14      Alarmclock (POSIX). Act: exit 
#defineSIGTERM 15      Termination(ANSI).  Act:exit
#defineSIGCHLD 17      Child status changed(POSIX).Act: ignore
#defineSIGCONT 18      Continue (POSIX).Act. ignore
#defineSIGSTOP 19      Stop, unblockable(POSIX). Act: stop