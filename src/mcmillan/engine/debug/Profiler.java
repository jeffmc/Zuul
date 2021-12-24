package mcmillan.engine.debug;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// https://perfetto.dev/docs/
// https://github.com/TheCherno/Hazel/blob/master/Hazel/src/Hazel/Debug/Instrumentor.h

public class Profiler {

	private static long runningScopeInstances = 0;
	
	private static Profiler instance;
	public static Profiler get() { 
		if (instance == null) instance = new Profiler();
		return instance;
	}
	
	private String sessionName = null;
	private FileOutputStream fos = null;
	
	public void beginSessionImpl(String name, File outputFile) {
		sessionName = name;
		try {
			outputFile.createNewFile();
			if (!outputFile.isFile()) throw new IllegalArgumentException("File passed isn't a file");
			fos = new FileOutputStream(outputFile);
			writeHeader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void beginSession(String name, File outputFile) { get().beginSessionImpl(name, outputFile); }
	
	public void endSessionImpl() {
		try {
			writeFooter();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sessionName + " Profiler session ended!");
	}
	public static void endSession() { get().endSessionImpl(); }
	public void write(String name, long start, long elapsedTime, long threadID) {
		StringBuilder sb = new StringBuilder();
		sb.append(",{");
		sb.append("\"cat\":\"function\",");
		sb.append("\"dur\":" + (elapsedTime) + ',');
		sb.append("\"name\":\"" + name + "\",");
		sb.append("\"ph\":\"X\",");
		sb.append("\"pid\":0,");
		sb.append("\"tid\":" + threadID + ",");
		sb.append("\"ts\":" + start);
		sb.append("}");
		synchronized(fos) {
			try {
				writeString(sb.toString());
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeString(String str) throws IOException {
		fos.write(str.getBytes());
	}
	
	private void writeHeader() throws IOException {
		synchronized(fos) {
			try {
				writeString("{\"otherData\": {},\"traceEvents\":[{}");
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void writeFooter() throws IOException {
		synchronized(fos) {
			try {
				writeString("]}");
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Scope startScopeImpl() {
		return new Scope();
	}
	public static Scope startScope() {
		return Profiler.get().startScopeImpl();
	}
	
	public class Scope {
		private long start;
		private boolean stopped = false;
		private String name;
		
		public Scope() {
			start = System.nanoTime();
			name = absoluteTrace(3);
			runningScopeInstances++;
		}
		public void stop() {
			if (stopped) return;
			
			long end = System.nanoTime();
			long elapsed = end - start;

			Profiler.get().write(name, start, elapsed, Thread.currentThread().getId());

			runningScopeInstances--;
			stopped = true;
		}
	}
	
	// Stack trace debugger methods
	public static String absoluteTrace(int stackTraceOffset) { 
		StackTraceElement e = stackTrace(1+stackTraceOffset);
		return e.getClassName() + "." + e.getMethodName(); 
	}
	public static String absoluteTrace() { return absoluteTrace(1); }
	
	public static StackTraceElement stackTrace(int stackTraceOffset) {
		return Thread.currentThread().getStackTrace()[2+stackTraceOffset]; }
	public static StackTraceElement stackTrace() { return stackTrace(1); }
	
	public static void printHeap() {
		/*
		Runtime rt = Runtime.getRuntime();
		System.out.println("Version: " + Runtime.version());
		System.out.println("Processors: " + rt.availableProcessors());
		long free = rt.freeMemory()/1024/1024, max = rt.maxMemory()/1024/1024, total = rt.totalMemory()/1024/1024;
		System.out.println(free + "MB FREE");
		System.out.println(max + "MB MAX");
		System.out.println(total + "MB TOTAL");
		System.out.println(total-free + "MB USED");
		System.out.println("Threads: " + Thread.activeCount());
		for (Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
			System.out.println(entry.getKey().getName() + ": ");
			for (StackTraceElement e : entry.getValue()) {
				System.out.println("  " + e.toString());
			}
		}*/
	}
	
}
