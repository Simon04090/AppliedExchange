package com.github.Simon04090.AppliedExchange.core;

import java.util.Arrays;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.ClassNode;
import squeek.asmhelper.com.github.Simon04090.AppliedExchange.ASMHelper;
import com.github.Simon04090.AppliedExchange.Logger;

public class AppliedExchangeClassTransformer implements IClassTransformer{
	private static final String[] classesBeingTransformed = {};

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass){
		int index = Arrays.asList(classesBeingTransformed).indexOf(name);
		return index != -1 ? transform(index, basicClass) : basicClass;
	}

	private static byte[] transform(int index, byte[] basicClass){
		Logger.info("[Core] Transforming:" + classesBeingTransformed[index]);
		try{
			ClassNode classNode = ASMHelper.readClassFromBytes(basicClass);

			switch (index){
				case 0 :
					// Transform the first class

			}
			return ASMHelper.writeClassToBytes(classNode);
		} catch (Exception e){
			e.printStackTrace();
		}
		return basicClass;
	}
}