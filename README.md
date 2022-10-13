# HCI-project
Install pip if it's not already installed.
Make sure venv is installed with "pip install venv".
Setup a virtual env with "python -m virtualenv <env_folder_name>". You might have to make a new folder to store the environment.
You should now be in the virtual environment named <env_folder_name>. If not, just go to <env_folder_name>/Scripts and run the "activate" script.
To install all dependencies, in the base directory of this repository run "pip install -r requirements.txt"
In your python IDE make sure to set the python interpreter to the one in the virtual environment. This should be at: <env_folder_name>/Scripts/python

Note: whisper requires ffmpeg for audio file conversion. If you need this then install ffmpeg on your main system. On Windows, you would have to install a package manager such as chocolatey or scoops and install ffmpeg through either of those. On Linux, use your distro's package manager to install ffmpeg.

Now you should be all set up!