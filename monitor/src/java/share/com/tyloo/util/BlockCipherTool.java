package com.tyloo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.BlowfishEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class BlockCipherTool {

	private static PaddedBufferedBlockCipher cipher = null;

	private static int keylength;

	private static final String keyStr = "AEBFADEBA9ABCDEF";
	static {
		keylength = 64;
		setEngine(new BlowfishEngine());

	}

	/**
	 * ��һ��,����Block Cipher�ļ�������, ��DES_Engine
	 * 
	 * @param block_cipher_engine
	 */
	private static void setEngine(BlockCipher block_cipher_engine) {
		/*
		 * Setup the DESede cipher engine, create a PaddedBufferedBlockCipher in
		 * CBC mode.
		 */
		cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(
				block_cipher_engine));
	}

	/**
	 * ��������������Կ
	 * 
	 * @param keyStr
	 */
	private static void init(boolean encrypt, String keyStr) {
		/**
		 * ��ʼ����Կ
		 */
		byte[] keybyte = new byte[keylength];
		keybyte = Hex.decode(keyStr);
		/**
		 * encrypt = true ���� encrypt = false ����
		 */
		cipher.init(encrypt, new KeyParameter(keybyte));
	}

	/**
	 * ���Ĳ���1����ִ�м���Encrypt���ڸ÷����У����input���ֶδ���ÿ�ζ�����
	 * ��inBlockSize���������ģ�����ͬ�������û��У���ˣ������ڼ��ܹ����У�����
	 * ��Ҫ���ж��룬�����ġ����ģ�Ψһ��Ӧ����inBlock��outBlock�������ǻ��ж�Ӧ�ġ�
	 * 
	 * @param input
	 * @param Key
	 * @return
	 */
	public static synchronized void encrypt(InputStream inputstream,
			OutputStream outputstream) {
		/**
		 * ��ʼ��������
		 */
		init(true, keyStr);
		int inBlockSize = cipher.getBlockSize() * 10;
		int outBlockSize = cipher.getOutputSize(inBlockSize);

		byte[] inblock = new byte[inBlockSize];
		byte[] outblock = new byte[outBlockSize];

		int inL;
		int outL;
		byte[] rv = null;
		try {
			while ((inL = inputstream.read(inblock, 0, inBlockSize)) > 0) {
				outL = cipher.processBytes(inblock, 0, inL, outblock, 0);
				/*
				 * Before we write anything out, we need to make sure that we've
				 * got something to write out.
				 */
				if (outL > 0) {
					rv = Hex.encode(outblock, 0, outL);
					outputstream.write(rv, 0, rv.length);
					// outputstream.write('\n');
				}
			}

			try {
				/*
				 * Now, process the bytes that are still buffered within the
				 * cipher.
				 */
				outL = cipher.doFinal(outblock, 0);
				if (outL > 0) {
					rv = Hex.encode(outblock, 0, outL);
					outputstream.write(rv, 0, rv.length);
					// outputstream.write('\n');
				}
			} catch (CryptoException ce) {
				ce.printStackTrace();
			}

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ���Ĳ���2����ִ�н���Decrypt
	 * 
	 * @param input
	 * @param Key
	 * @return
	 */
	public static synchronized void decrypt(InputStream inputstream,
			OutputStream outputstream) {
		/**
		 * ��ʼ������
		 */
		init(false, keyStr);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				inputstream));

		byte[] inblock = null;
		byte[] outblock = null;

		int outL;
		String rv = null;
		try {
			while ((rv = br.readLine()) != null) {
				inblock = Hex.decode(rv);
				outblock = new byte[cipher.getOutputSize(inblock.length)];

				outL = cipher.processBytes(inblock, 0, inblock.length,
						outblock, 0);
				/*
				 * Before we write anything out, we need to make sure that we've
				 * got something to write out.
				 */
				if (outL > 0) {
					outputstream.write(outblock, 0, outL);
				}
			}

			try {
				/*
				 * Now, process the bytes that are still buffered within the
				 * cipher.
				 */
				outL = cipher.doFinal(outblock, 0);
				if (outL > 0) {
					outputstream.write(outblock, 0, outL);
				}
			} catch (CryptoException ce) {
				System.err.println("decrypt error:" + rv);
				ce.printStackTrace();
			}

		} catch (DataLengthException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
