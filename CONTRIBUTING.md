# Contributing to the Yield Engine

First off all, thank you very much for helping with the development of the Yield Engine.🚀✨

## Table of Contents

- [Fork the project🍴](#fork-the-project)
- [Downloading your fork locally🔽](#downloading-your-fork-locally)
- [Opening the project📂](#opening-the-project)
- [Finding contributions to work on💡](#finding-contributions-to-work-on)
- [Making your way into the project🚧](#making-your-way-into-the-project)
- [Submitting a Pull Request📥](#submitting-a-pull-request)

## Fork the project

The first thing you want to do is fork the project. For more information, check the [fork a repo article](https://help.github.com/articles/fork-a-repo/).


## Downloading your fork locally

To install the project you need to have `git`

Clone your [fork](#fork-the-project) (be sure to replace *your-username* with your github account username):

    # Clone your fork
    git clone https://github.com/your-username/YieldEngine.git

    # Navigate to the created directory
    cd YieldEngine

## Opening the project

1.  Your environment needs to have `jdk` version >= 17

2.  Some Yield Engine modules have specific dependencies, your environment needs to have them installed to be able to compile them.
     - `core`: [Jamepad](https://github.com/williamahartman/Jamepad); [jbox2d-library-2.2.1.1](http://www.jbox2d.org/); [Aparapi](https://aparapi.github.io/); [Aparapi-jni](https://github.com/Syncleus/aparapi-jni); [Apache Commons BCEL](https://commons.apache.org/proper/commons-bcel/)
     - `openglimpl`: [jogl-all](https://jogamp.org/jogl/www/); [gluegen-rt](https://jogamp.org/gluegen/www/).
     - `openalimpl`: [lwjgl3](https://www.lwjgl.org/); [lwjgl3-openal](https://www.lwjgl.org/); [lwjgl3-stb](https://www.lwjgl.org/).
     - `lwjgl-utils`: [lwjgl3](https://www.lwjgl.org/).
     - `editor`:  [flatlaf](https://www.formdev.com/flatlaf/); [RSyntaxTextArea](https://github.com/bobbylight/RSyntaxTextArea); [AutoComplete](https://github.com/bobbylight/AutoComplete); [RSTALanguageSupport](https://github.com/bobbylight/AutoComplete). 

> Tip: Keep your `master` branch pointing at the original repository and make
> pull requests from branches on your fork. To do this, run:
>
> ```sh
> git remote add upstream https://github.com/yield-proj/YieldEngine.git
> git fetch upstream
> git branch --set-upstream-to=upstream/master master
> ```
>
> This will add the original repository as a "remote" called "upstream," then
> fetch the git information from that remote, then set your local `master`
> branch to use the upstream master branch whenever you run `git pull`. Then you
> can make all of your pull request branches based on this `master` branch.
> Whenever you want to update your version of `master`, do a regular `git pull`.🔄

## Finding contributions to work on

If you'd like to contribute, but don't have a project in mind, look at the [open issues](https://github.com/yield-proj/YieldEngine/issues) in this repository for some ideas. Also check the [help wanted](https://github.com/yield-proj/YieldEngine/labels/help%20wanted) its a great place to start.💡
In addition to written content, we really appreciate new implementations for different platforms or environments and examples for our library, such as mobile implementations and simple games.

## Making your way into the project

In your fork, make your change in a branch that's based on this repo's master branch.
Commit the change to your fork, using a clear and descriptive commit message.🔨🔧

## Submitting a Pull Request

Before you send a pull request, please be sure that:

1. You're working from the latest source on the **master** branch.
2. You check [existing open](https://github.com/yield-proj/YieldEngine/pulls), and [recently closed](https://github.com/yield-proj/YieldEngine/pulls?q=is%3Apr+is%3Aclosed), pull requests to be sure that someone else hasn't already addressed the problem.
3. You [create an issue](https://github.com/awsdocs/amazon-lightsail-developer-guide/issues/new) before working on a contribution that will take a significant amount of your time.
4. [Create a pull request](https://help.github.com/articles/creating-a-pull-request-from-a-fork/), answering any questions in the pull request form.📥✉️




Congratulation and welcome to the Yield Project! 🥳🥳🥳
