@echo off
call ant compile_stendhaltools server_build

set LOCALCLASSPATH=build\build_stendhaltools;build\build_server;build\build_server_maps;libs\marauroa.jar;libs\log4j.jar;libs\common-lang.jar;libs\groovy.jar;libs\mysql-connector-java-5.1.jar;libs\swing-layout.jar;.
javaw -cp "%LOCALCLASSPATH%" games.stendhal.tools.npcparsertestenv.TestEnvDlg
