    ORG     $1000          ; Set program start address (depends on system)

CR EQU $0D                 ; Carriage return
LF EQU $0A                 ; New Line


new_line DC.B   CR,LF,0    ; NEWLINE combo

teststring DC.B 'JACINTO test',0   ; test string

START:
    MOVE.W  #42, D1        ; Load the value 42 into register D1 (the integer to print)
    JSR     PRINT_INT      ; Jump to the PRINT_INT subroutine

    LEA     teststring, A1   ; point A1 to teststring
    JSR     PRINT_STRING      ; Jump to the PRINT_INT subroutine


    SIMHALT                ; Halt the simulator (optional)


PRINT_NEWLINE:
    MOVE.W  D0,-(A7)       ; Save D0 on stack
    MOVE.W  #14, D0  
    LEA     new_line, A1   ;
    TRAP    #15            ; System call to print newline
    MOVE.W  (A7)+, D0      ; Restore D0 from stack
    RTS

; ----------------------------------------------------------------------------- 
; PRINT_INT - PRINTS AN INTEGER 
; INPUT: D1 - INTEGER TO PRINT 
; OUTPUT: NONE 
; ----------------------------------------------------------------------------- 
PRINT_INT:
    MOVE.W  D0,-(A7)       ; Save D0 on stack (if needed elsewhere)
    MOVE.W  D1, D0         ; Move the integer in D1 to D0 (lower 16 bits)
    MOVE.W  #3, D0         ; Prepare system call to print integer (OS call #3)
    TRAP    #15            ; System call to print the integer
    JSR     PRINT_NEWLINE
    MOVE.W  (A7)+, D0      ; Restore D0 from stack
    RTS                    ; Return from subroutine
    
; ----------------------------------------------------------------------------- 
; PRINT_STRING - PRINTS A STRING
; INPUT: A1 - STRING TO PRINT 
; OUTPUT: NONE 
; ----------------------------------------------------------------------------- 
PRINT_STRING:
    MOVE.W  D0,-(A7)       ; Save D0 on stack (if needed elsewhere)
    MOVE.W  #14, D0         ; Prepare system call to print integer (OS call #3)
    TRAP    #15            ; System call to print the integer
    JSR     PRINT_NEWLINE
    MOVE.W  (A7)+, D0      ; Restore D0 from stack
    RTS                    ; Return from subroutine

SIMHALT: 
    TRAP    #15            ; System-specific halt operation (optional)
    RTS                    ; Return from main program

    END     START          ; End of program, define start point

*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
