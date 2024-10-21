    ORG     $1000          ; Set program start address (depends on system)

    INCLUDE "CONSTS.X68"


START:
    MOVE.W  #42, D1        ; Load the value 42 into register D1 (the integer to print)
    JSR     PRINT_INT      ; Jump to the PRINT_INT subroutine

    LEA     teststring, A1   ; point A1 to teststring
    JSR     PRINT_STRING      ; Jump to the PRINT_INT subroutine
    
    MOVE.W  #0, D1
    JSR     PRINT_BOOL
    
    JSR     READ_BOOL
    JSR     PRINT_BOOL


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
    
; ----------------------------------------------------------------------------- 
; PRINT_BOOL - PRINTS A STRING
; INPUT: D1 - BOOL TO PRINT
; OUTPUT: NONE 
; ----------------------------------------------------------------------------- 
PRINT_BOOL:
    MOVE.W  D0,-(A7)         ; Save D0 on stack (if needed elsewhere)
    CMP.W    #0, D1          ; Check if D1 is 0
    BNE     .BOOL_TRUE       ; If D1 is not zero, branch to BOOL_TRUE
    LEA     false_string, A1 ; Load address of "false" string into A1
    BRA     .END_BOOL        ; Branch to PRINT_BOOL_DONE
.BOOL_TRUE:
    LEA     true_string, A1  ; Load address of "true" string into A1
.END_BOOL:
    JSR     PRINT_STRING     ; Call PRINT_STRING to print the result
    MOVE.W  (A7)+, D0        ; Restore D0 from stack
    RTS                      ; Return from subroutine
 
 
; ----------------------------------------------------------------------------- 
; READ_INT - READS AN INTEGER
; INPUT: CONSOLE - INTEFER TO ADD
; OUTPUT: D1 - INTEGER 
; ----------------------------------------------------------------------------- 
 
READ_INT:
    MOVE.W  D0,-(A7)         ; Save D0 on stack (if needed elsewhere)
    MOVE.W  #4,D0            ; Sytem call to read int;
    TRAP    #15
    MOVE.W  (A7)+, D0        ; Restore D0 from stack
    RTS                      ; Return from subroutine

READ_STRING:
    MOVE.W  D0,-(A7)         ; Save D0 on stack (if needed elsewhere)
    MOVE.W  #2,D0            ; Sytem call to read string;
    TRAP    #15
    MOVE.W  (A7)+, D0        ; Restore D0 from stack
    RTS                      ; Return from subroutine

READ_BOOL:
    MOVE.W  D0,-(A7)         ; Save D0 on stack
    LEA     bool_buffer, A1  ; Load the address of the boolean input buffer
    JSR     READ_STRING      ; Read user input string into bool_buffer

    MOVE.B  (A1),D0          ; Move the first character of the buffer into D0
    CMP.B   #'F',D0          ; Compare with 'F'
    BEQ     .READ_FALSE      ; If 'F', jump to READ_FALSE
    CMP.B   #'f',D0          ; Compare with 'f'
    BEQ     .READ_FALSE      ; If 'f', jump to READ_FALSE

    MOVE.W  #1, D1           ; If not 'F' or 'f', assume it's 'T'/'t', so D1 = 1
    BRA     .END_BOOL        ; Branch to the end

.READ_FALSE:
    MOVE.W  #0, D1           ; If 'F' or 'f', D1 = 0

.END_BOOL:
    MOVE.W  (A7)+, D0        ; Restore D0 from stack
    RTS                      ; Return from subroutine


;---------------------------------------------------------------------
;TEST VARS
;-----------------------------------------------------------------------

teststring DC.B 'JACINTO test',0   ; test string
               

    END     START          ; End of program, define start point





*~Font name~Courier New~
*~Font size~10~
*~Tab type~1~
*~Tab size~4~
