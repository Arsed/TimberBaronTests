@echo off

pushd ilm-fph-web 
call java -jar fph-automation-tests-general.jar %1 %2 %3
popd