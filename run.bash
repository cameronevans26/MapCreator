#!/bin/bash
set -u -e
javac Game.java GameView.java Controller.java Model.java Json.java
java Game

