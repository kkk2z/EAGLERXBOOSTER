package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.jemalloc.JEmalloc;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class EaglerLWJGLIntBuffer implements IntBuffer {
	
	final long address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;
	
	private static final int SHIFT = 2;
	
	EaglerLWJGLIntBuffer(long address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	EaglerLWJGLIntBuffer(long address, int capacity, int position, int limit, int mark, boolean original) {
		this.address = address;
		this.capacity = capacity;
		this.position = position;
		this.limit = limit;
		this.mark = mark;
		this.original = original;
	}
	
	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public int position() {
		return position;
	}

	@Override
	public int limit() {
		return limit;
	}

	@Override
	public int remaining() {
		return limit - position;
	}

	@Override
	public boolean hasRemaining() {
		return position < limit;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean hasArray() {
		return false;
	}

	@Override
	public Object array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int arrayOffset() {
		return position;
	}

	@Override
	public IntBuffer slice() {
		return new EaglerLWJGLIntBuffer(address + (position << SHIFT), limit - position, false);
	}

	@Override
	public IntBuffer duplicate() {
		return new EaglerLWJGLIntBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public IntBuffer asReadOnlyBuffer() {
		return new EaglerLWJGLIntBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public int get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return MemoryUtil.memGetInt(address + ((position++) << SHIFT));
	}

	@Override
	public IntBuffer put(int b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutInt(address + ((position++) << SHIFT), b);
		return this;
	}

	@Override
	public int get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetInt(address + (index << SHIFT));
	}

	@Override
	public IntBuffer put(int index, int b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutInt(address + (index << SHIFT), b);
		return this;
	}

	@Override
	public int getElement(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetInt(address + (index << SHIFT));
	}

	@Override
	public void putElement(int index, int value) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutInt(address + (index << SHIFT), value);
	}

	@Override
	public IntBuffer get(int[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = MemoryUtil.memGetInt(address + ((position + i) << SHIFT));
		}
		position += length;
		return this;
	}

	@Override
	public IntBuffer get(int[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = MemoryUtil.memGetInt(address + ((position + i) << SHIFT));
		}
		position += dst.length;
		return this;
	}

	@Override
	public IntBuffer put(IntBuffer src) {
		if(src instanceof EaglerLWJGLIntBuffer) {
			EaglerLWJGLIntBuffer c = (EaglerLWJGLIntBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			MemoryUtil.memCopy(c.address + (c.position << SHIFT), address + (position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			for(int i = 0; i < l; ++i) {
				MemoryUtil.memPutInt(address + ((position + l) << SHIFT), src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public IntBuffer put(int[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			MemoryUtil.memPutInt(address + ((position + i) << SHIFT), src[offset + i]);
		}
		position += length;
		return this;
	}

	@Override
	public IntBuffer put(int[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			MemoryUtil.memPutInt(address + ((position + i) << SHIFT), src[i]);
		}
		position += src.length;
		return this;
	}

	@Override
	public int getArrayOffset() {
		return position;
	}

	@Override
	public IntBuffer compact() {
		if(limit > capacity) throw new ArrayIndexOutOfBoundsException(limit);
		if(position > limit) throw new ArrayIndexOutOfBoundsException(position);
		
		if(position == limit) {
			return new EaglerLWJGLIntBuffer(0l, 0, false);
		}
		
		int newLen = limit - position;
		long newAlloc = JEmalloc.nje_malloc(newLen);
		MemoryUtil.memCopy(address + (position << SHIFT), newAlloc, newLen << SHIFT);
		
		return new EaglerLWJGLIntBuffer(newAlloc, newLen, true);
	}

	@Override
	public boolean isDirect() {
		return true;
	}

	@Override
	public IntBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public IntBuffer reset() {
		int m = mark;
		if(m < 0) throw new ArrayIndexOutOfBoundsException("Invalid mark: " + m);
		position = m;
		return this;
	}

	@Override
	public IntBuffer clear() {
		position = 0;
		limit = capacity;
		mark = -1;
		return this;
	}

	@Override
	public IntBuffer flip() {
		limit = position;
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public IntBuffer rewind() {
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public IntBuffer limit(int newLimit) {
		if(newLimit < 0 || newLimit > capacity) throw new ArrayIndexOutOfBoundsException(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public IntBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw new ArrayIndexOutOfBoundsException(newPosition);
		position = newPosition;
		return this;
	}

}
