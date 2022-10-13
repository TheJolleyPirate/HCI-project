import datetime
import whisper
import keyboard
import numpy as np
import pyaudio

model = whisper.load_model("tiny.en")
running: bool = True
audioData: np.ndarray
pa = pyaudio.PyAudio()

def audio_record_callback(in_data, frame_count, time_info, status_flag):
    global audioData
    data = np.frombuffer(in_data, dtype=np.float32)
    audioData = np.append(audioData, data)
    return None, status_flag

#scan for audio devices
# for i in range(pa.get_device_count()):
#     print(pa.get_device_info_by_index(i))

micDeviceID = 9
micInfo = pa.get_device_info_by_index(micDeviceID)
print(micInfo)

while running:
    audioData = np.ndarray(0)
    print("Press space to begin recording...")
    keyboard.wait("space")

    print("Recording has begun, press space again to stop...")
    # Record audio and store as audioData
    stream_in = pa.open(format=pyaudio.paFloat32, channels=1, rate=16000,
            input=True, stream_callback=audio_record_callback)
    keyboard.wait("space")
    stream_in.close()

    print("Transcribing audio please wait...")
    audioData = audioData.astype(np.float32)
    # print(audioData)
    # stream_out = pa.open(format=pyaudio.paFloat32, channels=1, rate=16000, output=True) #playback audio for debugging
    # stream_out.write(audioData.tobytes())
    start_time = datetime.datetime.now()

    #Transcribe audio to text
    transcription = model.transcribe(audio=audioData, language="English")
    end_time = datetime.datetime.now()
    print(f"Transcription Text:\n{transcription['text']}\nTime Taken: {end_time-start_time}")