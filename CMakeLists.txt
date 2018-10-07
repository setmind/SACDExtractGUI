cmake_minimum_required (VERSION 2.6)
find_package(Java REQUIRED)
include(UseJava)

execute_process(
    COMMAND git describe --tags --dirty --abbrev=8
    OUTPUT_VARIABLE GIT_COMMIT_HASH
    RESULT_VARIABLE GIT_COMMIT_HASH_RESULT
    OUTPUT_STRIP_TRAILING_WHITESPACE
)

execute_process(
    COMMAND git remote get-url origin
    OUTPUT_VARIABLE GIT_REPO_URL
    RESULT_VARIABLE GIT_REPO_URL_RESULT
    OUTPUT_STRIP_TRAILING_WHITESPACE
)
if(${GIT_COMMIT_HASH_RESULT} GREATER 0)
    add_definitions("-DGIT_COMMIT_HASH=NA")
else(${GIT_COMMIT_HASH_RESULT} GREATER 0)
    add_definitions("-DGIT_COMMIT_HASH=${GIT_COMMIT_HASH}")
    MESSAGE(STATUS "git commit hash: ${GIT_COMMIT_HASH}")
endif(${GIT_COMMIT_HASH_RESULT} GREATER 0)

if(${GIT_REPO_URL_RESULT} GREATER 0)
    add_definitions("-DGIT_REPO_URL=NA")
else(${GIT_REPO_URL_RESULT} GREATER 0)
    add_definitions("-DGIT_REPO_URL=\"${GIT_REPO_URL}\"")
    MESSAGE(STATUS "git repo url: ${GIT_REPO_URL}")
endif(${GIT_REPO_URL_RESULT} GREATER 0)

file(WRITE MANIFEST.MF "Main-Class: SACDExtractGUI\n")
file(WRITE Version.properties "version=${GIT_COMMIT_HASH}\n")
file(APPEND Version.properties "repository=${GIT_REPO_URL}\n")

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "1.7" "-target" "1.7")

project(SACDExtractGUI Java)
add_jar(SACDExtractGUI MANIFEST MANIFEST.MF SACDExtractGUI.java AppFrame.java ExecThread.java InputPanel.java ProcessingPanel.java ProgramState.java CheckBoxState.java GuiControl.java OutputPanel.java ProgramPanel.java Version.properties)
