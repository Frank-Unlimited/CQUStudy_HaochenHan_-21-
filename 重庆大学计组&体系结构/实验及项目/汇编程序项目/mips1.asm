#���������̣�
	#���븡������������ -> �������š�ָ����β���Լ���ƫ��ָ�� -> �������㹦������ת�����㹦�� -> �����ͬ���Ʊ�ʾ�Ľ��
.data		#���ݶΣ�������ڴ��У�
#��������������������ʹ�õı�������
	num1:		.space 20		#0($s5)��num1��4($s5)������λ��8($s5)��ָ����12($s5)��β����16($s5)��ƫ��
	num2:		.space 20		#0($s6)��num2��4($s6)������λ��8($s6)��ָ����12($s6)��β����16($s6)��ƫ��
	result:		.space 16		#0($s7)��������С�������4($s7)��ָ����8($s7)������λ��12($s7)��IEEE754�����	
	
	Tips1:		.asciiz "Please input the first float:\0"
	Tips2:		.asciiz "Please input the second float:\0"
	Tips3:     	.asciiz "Please choose one function: 0 for exit, 1 for add, 2 for sub: \0"
	Tips4:		.asciiz "Sorry, your function number is out of index! Please input right function number between 0 and 2. \n"
	Tips5:		.asciiz "Exit\0"
	Overflow1:	.asciiz "Up Overflow Excption!\n"
	Overflow2:	.asciiz "Down Overflow Excption!\n"
	Precision:	.asciiz "Precision loss!\n"
	Ansb:		.asciiz	"The binary result of calculation is:\0"
	Ansh:		.asciiz"The hexadecimal result of calculation is:\0"
	Ansd:		.asciiz	"The decimal result of calculation is:\0"
	NewLine:   	.asciiz "\n"
	
.text		#�����
	main:	#��ʼִ��
		#��num1��num2���׵�ַ���ڴ�д��Ĵ���
		la  	$s5, 	num1			#��num1���׵�ַ���浽$s5�Ĵ���
	   	la  	$s6,	num2			#��num2���׵�ַ���浽$s6�Ĵ���
	   	#��ת�����뺯�������ո�����num1��num2����������š�ָ����β���ʹ�ƫ��ָ��
	   	jal Input_funct				#jal�Ƚ���ǰPC����$ra����ת
		#������㹦�ܣ�0�˳���1�ӷ���2������
		la	$a0,	Tips3
		li	$v0,	4
		syscall					#��ӡ"Please choose one function: 0 for exit, 1 for add, 2 for sub: \0"
		li	$v0,	5
		syscall					#����ϵͳ$v0=5��ȡ���������ֵ������$v0
		#��ʱ$v0�洢���㹦���룬�ֱ�Ƚ�0��1��2������ת����Ӧ�����������ڸ�����������쳣
		li	$t0,	1
		beq	$v0,	$t0,	add_funct       #�ӷ�
		li	$t0,	2                   
		beq	$v0,	$t0,	sub_funct        #����                
		li	$t0,	0
		beq	$v0,	$t0,	exit_funct  	#�˳�
		bne	$v0,	$t0,	default_funct	#����Ĳ���0-4
	
#�����븡����������Ӧ�Ĵ�������ȡ���š�ָ����β���ʹ�ƫ��ָ��
Input_funct:
	#��ӡ"Please input the first float:\0"
	la $a0,Tips1
	li $v0,4
	syscall
	#ϵͳ���ö�ȡ����ĸ�����������$f0
	li $v0,6
	syscall
	#��$f0�е����ݴ���$s1�������ڴ�
	mfc1 $s1,$f0
	sw $s1,0($s5)
       	#��ӡ"Please input the second float:\0"
	la $a0,Tips2
	li $v0,4
	syscall
 	#ϵͳ���ö�ȡ����ĸ�����������$f0
	li $v0,6
	syscall
	#��$f0�е����ݴ���$s2�������ڴ�
	mfc1 $s2,$f0
	sw $s2,0($s6)
	#��num1����λ����4($s5)
	andi $t1,$s1,0x80000000		#0x80000000Ϊ16��������������Ϊ32'b1000_...._0000����$s1�е�num1��λ��õ�num1����λ��31λ��
	srl  $t1,$t1,31              	#����31λ����
	sw   $t1,4($s5)
	#��num2����λ����4($s6)
	andi $t1,$s2,0x80000000    
	srl  $t1,$t1,31
	sw   $t1,4($s6)
	#��num1ָ������8($s5)
	andi $t1,$s1,0x7f800000		#������Ϊ32'b0111_1111_1000_..._0000����$s1�е�num1��λ��õ�num1ָ����23~30λ��
	srl  $t2,$t1,23              	#�Ҷ���
	sw   $t2,8($s5)
	#��num2ָ������8($s6)
	andi $t1,$s2,0x7f800000      
	srl  $t3,$t1,23
	sw   $t3,8($s6)
	#��num1β������12($s5)
	andi  $t1,$s1,0x007fffff     	#������Ϊ32'b0000_..._0111_1111_..._1111����$s1�е�num1��λ��õ�num1β����0~22λ��
	sw    $t1,12($s5)
	#��num2β������12($s6)
	andi $t1,$s2,0x007fffff
	sw   $t1,12($s6)
	#��num1��ƫ��ָ������16($s5)
	addi $t4,$0,0x0000007f 		#ƫ��127
	sub  $t1,$t2,$t4             	#ָ��-ƫ�׵õ���ƫ��ָ��
	sw   $t1,16($s5)
	#��num2��ƫ��ָ������16($s6)
	sub  $t1,$t3,$t4
	sw   $t1,16($s6)
	#��ת�ص��ú���ǰ��PC��������ָ��Ĵ���$ra�У�
	jr $ra

#�ӷ����̣�ȡnum1��num2�ķ���λ���ס�β��->��ȫβ��������λ->�Խ�->ִ�мӷ�����->���
#add_funct->getAdd->(Align_exp)->beginAdd->Add_Same_sign/Add_Diff_sign->NumSRL->showAns
add_funct:
	jal 	getAdd
	jal	binary
	jal	hex
	j 	main				#����ִ����ϣ�������������ͷ
getAdd:
	#ȡnum1��num2�ķ���λ
	lw	$s0,	4($s5) 			#$s0��num1�ķ���λ��$s1��num2�ķ���λ
	lw	$s1,	4($s6)
	#ȡnum1��num2�Ľ�
	lw	$s2,	8($s5) 			#$s2��num1�Ľף�$s3��num2�Ľ�
	lw	$s3,	8($s6)
	#ȡnum1��num2��β��
	lw	$s4,	12($s5)			#$s4��num1��β����$s5��num2��β��
	lw	$s5,	12($s6)
	#��ȫβ��������λ1
	ori	$s4,	$s4,	0x00800000	#������λ1��ȫ
	ori	$s5,	$s5,	0x00800000
	#�Խ�
	sub	$t0,	$s2,	$s3 		#�Ƚ�num1��num2�Ľ�����ָ������С
	bltz	$t0,	Align_exp1 		#$t0С��0�������num1�Ľ�С��num2���轫num1���ƶԽ�
	bgtz	$t0,	Align_exp2 		#$t0����0���轫num2���ƶԽ�
	beqz	$t0,	beginAdd		#����������ͬ����ֱ�����
#�Խף�����������������ͬ���������ǰ��Ҫ�ȶԽס�������С���������Ķ��룬��Ϊ��֮�ή�ͽ���������ľ���
#�Խ׹��̲��õݹ飬����С������1λ���жϻص�Align_exp������ͷ��ʼ���
Align_exp1:	#num1�Ľ�С��num2�Ľף�num1����+1��β������
	addi	$s2,	$s2,	1		#num1����+1
	srl	$s4,	$s4,	1		#num1β������
	sub	$t0,	$s2,	$s3 		#ѭ���ж�
	bltz	$t0,	Align_exp1		#branch if less than zero
	beqz	$t0,	beginAdd		#branch if equal zero�������
Align_exp2:	#num1�Ľ״���num2�Ľף�num2����+1��β������
	addi	$s3,	$s3,	1
	srl	$s5,	$s5,	1
	sub	$t0,	$s2,	$s3
	bgtz	$t0,	Align_exp2
	beqz	$t0,	beginAdd
#��ʱnum1��num2������ͬ���жϷ��ź�������
beginAdd:
	xor	$t1,	$s0,	$s1 		#��λ����ж�num1��num2�����Ƿ���ͬ����ͬ��$t1��32'b0����ͬ��32'b1��
	beq	$t1,	$zero,	Add_Same_sign 	#num1��num2������ͬ����ֱ�Ӽ�(Add_Same_sign)
	j	Add_Diff_sign 			#num1��num2���Ų�ͬ����ת��(Add_Diff_sign)
#num1��num2������ͬ���
Add_Same_sign:
	add	$t2,	$s4,	$s5		#β����Ӻ�Ľ����Ϊ�����β��������Ҫ���ж��Ƿ����
	sge	$t3,	$t2,	0x01000000	#set if greater or equal �ж�����
	#��Ϊ�����޷���23bit����������ӣ��������ĵ�24λΪ1�����������磬��Ҫ����β��������+1
	bgtz	$t3,	NumSRL			#������β������ 
	j	showAns				#���������ת������������
#num1��num2������ͬ��Ӻ����������Ҫβ�����ƣ�����+1
NumSRL:
	srl	$t2,	$t2,	1		#β������
	addi	$s2,	$s2,	1		#����+1
	j	showAns				#��ʱ������β����ȷ���������
#num1��num2���Ų�ͬ���
Add_Diff_sign:
	sub	$t2,	$s4,	$s5		#���Ų�ͬ��������൱��������ټӷ��ţ������ܳ���β���������磩���С�����磩�����
	bgtz	$t2,	Add_Diff_sign1		#���num1��β����num2������ת��Add_Diff_sign1�������num1ͬ�ţ�
	bltz	$t2,	Add_Diff_sign2		#���num1��β����num2С������ת��Add_Diff_sign2�������num2ͬ�ţ�
	j	show0				#������ǵľ���ֵ��ȣ�����Ϊ0��������ת�����������
#num1��num2���Ų�ͬ��ӣ�num1β����num2�����ǰ��Ҫ���ж��Ƿ����������
Add_Diff_sign1:
	blt	$t2,	0x00800000,	Add_Diff_sign11		#β��̫С���������ƣ�������
	bge	$t2,	0x01000000,	Add_Diff_sign12		#���β��û�й�С����ô����Ҫ�ж�����
	j	showAns						#�Ȳ�����Ҳ������Ľ��������ֱ�����
#num1��num2���Ų�ͬ��ӣ�num1β����num2�󣬽��β��̫С
Add_Diff_sign11:
	sll	$t2,	$t2,	1				#��������β��
	subi	$s2,	$s2,	1				#����-1
	blt	$t2,	0x00800000,	Add_Diff_sign11		#ѭ������β��
	j	showAns
#num1��num2���Ų�ͬ��ӣ�num1β����num2�󣬽��β��̫��
Add_Diff_sign12:
	srl	$t2,	$t2,	1				#������Сβ��
	addi	$s2,	$s2,	1				#����+1
	bge	$t2,	0x01000000,	Add_Diff_sign12
	j	showAns
#num1��num2���Ų�ͬ��ӣ�num1β����num2С
Add_Diff_sign2:
	sub	$t2,	$s5,	$s4				#��$t2������Ϊ��
	xori    $s0     $s0     0x00000001			#�����num2ͬ��
	j	Add_Diff_sign1					#ģ�鸴��

#�������Ը��üӷ�ģ�飨��num2����ȡ�����ɣ�
sub_funct:
	lw 	$t1,	4($s6)					#num2�ķ���λ����$t1
	xori 	$t1,	$t1,	1				#��num2����λ��λ���ȡ����
	sw 	$t1,	4($s6)
	jal 	getAdd
	jal	binary
	jal	hex
	j main
#op����0ʱ�˳�
exit_funct:
	la  	$a0, 	Tips5     
	li  	$v0, 	4           
	syscall
	li 	$v0,	10					#��������
	syscall
#op�����Ϲ淶ʱ�ص�main��ͷ��������
default_funct:
	la $a0,Tips4
	li $v0,4
	syscall
	j main

#��ӡ��ͬ���ƵĽ��
showAns:
	#��ӡʮ���ƽ��
	li	$v0,	4
	la	$a0,	Ansd
	syscall
	#�ж��Ƿ�����
	#�����ȸ�����ֻ��8λָ��λ������ƫ�ף�
	#��С��0��ԭָ��С��-128����������磻
	#������255��ԭָ������127������������������ԭ���޷�ͨ��ƫ�ƽ����
	blt 	$s3,	0,	downOverflow	#���磬��ת��downOverflow��ӡ"Down Overflow Excption!"
	#�ж�������
	bgt	$s2,	255,	upOverflow	#���磬��ת��upOverflow��ӡ"Up Overflow Excption!"
	#�������ԭ��31λ����
	sll	$s0,	$s0,	31		#ǰ��Ĵ����Ѿ�������ķ���λ����$s0��ֱ�����������λ
	sll	$s2,	$s2,	23		#��ָ��λ�ƶ�����Ӧλ��
	sll	$t2,	$t2,	9		#$t2�д���������β����Ϊ��ֹβ��23λ�����������������Ƶķ�ʽֻ����0~22λ����ֵ
	srl	$t2,	$t2,	9
	add	$s2,	$s2,	$t2		#����λ+ָ��+β��=���
	add	$s0,	$s0,	$s2
	mtc1    $s0,	$f12		
	#���
	li 	$v0,	2									
	syscall 
	li	$v0,	4
	la	$a0,	NewLine
	syscall
	jr $ra
#���ս������
downOverflow:
	la 	$a0,	Overflow2
	li	$v0,	4
	syscall					#��ӡ"Down Overflow Excption!"
	jr $ra
#���ս������
upOverflow:
	la 	$a0,	Overflow1
	li	$v0,	4
	syscall					#��ӡ"Up Overflow Excption!"
	jr $ra

# ת���ɶ�����
binary:
	li	$v0,	4
	la	$a0,	Ansb
	syscall					#��ӡ"The binary result of calculation is:"
	addu	$t5,	$s0,	$0		#$s0�д�ŵ�IEEE754��׼�ļ�����
	add	$t6,	$t5,	$0
	addi	$t7,	$0,	32
	addi	$t8,	$t0,	0x80000000	#�жϽ��ָ��������
	addi	$t9,	$0,	0
binary_transfer:				#ִ����binary˳��ִ��binary_transfer
	#$t6:IEEE754��׼�ļ�����  $t7:32'b0000_..._0100_0000  $t8:�����ָ������
	subi	$t7,	$t7,	1
	and	$t9,	$t6,	$t8
	srl	$t8,	$t8,	1
	srlv	$t9,	$t9,	$t7
	add	$a0,	$t9,	$0
	li	$v0,	1
	syscall
	beq	$t7,	$t0,	back
	j	binary_transfer
#ת����ʮ�����ƣ���4λ������ת1λʮ�����Ƽ��ɣ�
hex:
	li	$v0,	4
	la	$a0,	Ansh
	syscall
	addi	$t7,	$0,	8
	add	$t6,	$t5,	$0
	add	$t9,	$t5,	$0
hex_transfer:
	beq	$t7,	$0,	back
	subi	$t7,	$t7,	1
	srl	$t9,	$t6,	28
	sll	$t6,	$t6,	4
	bgt	$t9,	9,	getAscii
	li	$v0,	1
	addi	$a0,	$t9,	0
	syscall
	j	hex_transfer
#ת��Ϊascii��
getAscii:
	addi	$t9,	$t9,	55
	li	$v0,	11
	add	$a0,	$t9,	$0
	syscall
	j	hex_transfer
#������Ϊ0�����
show0:
	mtc1    $zero,	$f12	
	li 	$v0,	2     
	syscall
	jr $ra
#ת��Ϊָ�����������ص����ú���ǰ��ָ��
back:				
	la    	$a0,	NewLine		
	li    	$v0,	4
	syscall
	jr $ra