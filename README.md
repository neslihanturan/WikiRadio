# WikiRadio
WikiRadio is started as one of Outreachy round 13 project. It is a radio application which plays audio files from commons.wikimedia.org and/or tts files of random summaries from en.wikipedia.org. After internship, it is stil being developped.

Here is a [demo](https://www.youtube.com/watch?v=aioZ8K1YsKA&feature=youtu.be)

# Installation
On comman line, please type:<br />
  ```
  cd DesiredWorkingDirectory<br />
  git clone https://github.com/neslihanturan/WikiRadio.git<br />
  ```

**Build with Android Studio or IntelliJ**<br />
1 - Open Android Studio <br />
2 - Select File -> Import Project<br />
3 - Find the directory you saved and select build.gradle file<br />

# Code Structure
**User Interractions**<br />
RadioActivity.java - Main activity of the app. User can interract by action buttons on it.<br />
NotificationService.java - Background service for notification operations<br />

**CacheController**<br />
WikipediaSummaryCacheController.java - Caches tts files on background<br />
CommonsCacheController.java - Caches audio files from commons<br />

**Action**<br />
AudioSourceSelector.java - Choose audio source based from user prefrences, as commons audio files, tts files or both<br />
AudioFileButtonListener.java - Includes the methods to operate actions for commons audio files<br />
TTSButtonListener.java - Includes the methods to operate actions for tts files<br />

**Model**<br />
AudioFile.java - It is a data type to express audio files from commons<br />
TTSFile.java - It is a data type to express audio files from tts source<br />

**MediaPlayer**<br />
MediaPlayerController.java - It is a interface for mediaplayer actions, makes state checks<br />
SingleMediaPlayer.java - Singletone object to make sure there is only one mediaplayer object at a time<br />
MediaPlayerState.java - Madiaplayer states table<br />

**Used Libraries**<br />
[DanikulaVideoCache](https://github.com/danikula/AndroidVideoCache) is used for background caching of commons files<br />
[Retrofit2](https://github.com/square/retrofit) is used for network operations<br />

# UML Diagram<br />
[Here](https://mega.nz/#!edAmASCa!-D33GBfpJKr98KlTF8ua6N5sMoS_VDZXjdsNVGCBqBM)
# Contributing<br />
New contributors are welcome. You can directly connect with me tur.neslihan(at)gmail(dot)com. Please create a new issue for the problem  (there are several of them:)) you encountered or solve an existing issue.<br />
