
CR EQU $0D                    ; Carriage return
LF EQU $0A                    ; New Line


new_line DC.B   CR,LF,0       ; NEWLINE combo
true_string DC.B 'true',0     ; true string
false_string DC.B 'false',0   ; false string

input_buffer DS.B 64          ; Define a buffer to store 2 characters (one for 't'/'f', and one for the null terminator)




*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
