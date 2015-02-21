package com.eff.musicSheetHelper;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {
	private static final int RECORDER_SAMPLE_RATE = 8000;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static final int BUFFER_ELEMENTS_REC = 1024;
	private static final int BYTES_PER_ELEMENT = 2;

	private Subscription mNoteSubscription;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView frequencyTextView = (TextView) findViewById(R.id.main_note);
		final TextView startStopTextView = (TextView) findViewById(R.id.main_start_stop);
		startStopTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				if (mNoteSubscription != null) {
					startStopTextView.setText(R.string.start);
					mNoteSubscription.unsubscribe();
					mNoteSubscription = null;
				} else {
					startStopTextView.setText(R.string.stop);
					mNoteSubscription = getRecordingObservable().subscribe(new Action1<Note>() {
						@Override
						public void call(final Note note) {
							frequencyTextView.setText(note.getName());
						}
					});
				}
			}
		});
	}

	private Observable<Note> getRecordingObservable() {
		return Observable.create(new Observable.OnSubscribe<Note>() {
			@Override
			public void call(final Subscriber<? super Note> subscriber) {
				final AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
						RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
						RECORDER_AUDIO_ENCODING, BUFFER_ELEMENTS_REC * BYTES_PER_ELEMENT);
				recorder.startRecording();

				short sData[] = new short[BUFFER_ELEMENTS_REC];

				while (!subscriber.isUnsubscribed()) {
					// gets the voice output from microphone to byte format
					recorder.read(sData, 0, BUFFER_ELEMENTS_REC);
					byte bData[] = convertShort2Byte(sData);
					final int frequency = calculateFrequency(bData);
					final Note note = Note.findClosestNote(frequency);
					subscriber.onNext(note);
				}
				recorder.stop();
				recorder.release();
				subscriber.onCompleted();
			}
		}).subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread());
	}

	private byte[] convertShort2Byte(short[] sData) {
		byte[] bytes = new byte[sData.length * 2];
		for (int i = 0; i < sData.length; i++) {
			bytes[i * 2] = (byte) (sData[i] & 0x00FF);
			bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
			sData[i] = 0;
		}
		return bytes;
	}

	private int calculateFrequency(byte[] signal) {
		final int mNumberOfFFTPoints = 1024;
		double mMaxFFTSample;
		int freq;
		double temp;
		Complex[] y;
		Complex[] complexSignal = new Complex[mNumberOfFFTPoints];
		double[] absSignal = new double[mNumberOfFFTPoints / 2];

		for (int i = 0; i < mNumberOfFFTPoints; i++) {
			temp = (double) ((signal[2 * i] & 0xFF) | (signal[2 * i + 1] << 8)) / 32768.0F;
			complexSignal[i] = new Complex(temp, 0.0);
		}

		y = FFT.fft(complexSignal);

		mMaxFFTSample = 0.0;
		int mPeakPos = 0;
		for (int i = 0; i < (mNumberOfFFTPoints / 2); i++) {
			absSignal[i] = Math.sqrt(Math.pow(y[i].re(), 2) + Math.pow(y[i].im(), 2));
			if (absSignal[i] > mMaxFFTSample) {
				mMaxFFTSample = absSignal[i];
				mPeakPos = i;
			}
		}

		freq = mPeakPos * RECORDER_SAMPLE_RATE / mNumberOfFFTPoints;

		return freq;
	}

	@Override
	protected void onDestroy() {
		if (mNoteSubscription != null) {
			mNoteSubscription.unsubscribe();
		}
		super.onDestroy();
	}
}
