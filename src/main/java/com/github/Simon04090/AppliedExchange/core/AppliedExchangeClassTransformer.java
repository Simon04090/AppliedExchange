package com.github.Simon04090.AppliedExchange.core;

import static org.objectweb.asm.Opcodes.*;
import java.util.Arrays;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import squeek.asmhelper.com.github.Simon04090.AppliedExchange.ASMHelper;
import com.github.Simon04090.AppliedExchange.Logger;

public class AppliedExchangeClassTransformer implements IClassTransformer{
	private static final String[] classesBeingTransformed = {"appeng.client.me.ItemRepo"};

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
					transformItemRepo(classNode);
					break;
			}
			return ASMHelper.writeClassToBytes(classNode);
		} catch (Exception e){
			e.printStackTrace();
		}
		return basicClass;
	}

	private static void transformItemRepo(ClassNode classNode){
		final String updateView_CLASS = "appeng.client.me.ItemRepo";
		final String updateView = "appeng.client.me.ItemRepo.updateView";
		final String updateView_DESC = "()V";
		Logger.info(updateView_DESC);
		MethodNode method = ASMHelper.findMethodNodeOfClass(classNode, "updateView", updateView_DESC);
		if (method != null){
			Logger.info("Transforming " + method.name);
			/*
			 * Find the first two instructions of the else statement (ALOAD,
			 * GETFIELD)
			 */
			InsnList pattern = new InsnList();
			pattern.add(new VarInsnNode(ALOAD, 0));
			pattern.add(new FieldInsnNode(GETFIELD, updateView_CLASS.replace('.', '/'), "view", "Ljava/util/ArrayList;"));
			pattern.add(new FieldInsnNode(GETSTATIC, "appeng/util/ItemSorters", "CONFIG_BASED_SORT_BY_NAME", "Ljava/util/Comparator;"));
			AbstractInsnNode targetNode = ASMHelper.find(method.instructions, pattern);
			InsnList toInject = new InsnList();
			if (targetNode != null){
				Logger.info("Found targetNode");
				/*
				 * Inject before the else statement: else if(SortBy ==
				 * SortOrder.EMC) { com.github.Simon04090
				 * .AppliedExchange.core.EMCSorter.Direction =
				 * (appeng.api.config.SortDir) SortDir; Collections.sort(
				 * this.view, com.github.Simon04090
				 * .AppliedExchange.core.EMCSorter.SORT_BY_EMC ); }
				 */
				toInject.add(new LdcInsnNode("This is before the else if"));
				toInject.add(new InsnNode(ICONST_0));
				toInject.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
				toInject.add(new MethodInsnNode(INVOKESTATIC, "com/github/Simon04090/AppliedExchange/Logger", "info", "(Ljava/lang/String;[Ljava/lang/Object;)V", false));
				toInject.add(new VarInsnNode(ALOAD, 7));
				toInject.add(new FieldInsnNode(GETSTATIC, "appeng/api/config/SortOrder", "EMC", "Lappeng/api/config/SortOrder;"));
				LabelNode label1 = new LabelNode();
				toInject.add(new JumpInsnNode(IF_ACMPNE, label1));
				toInject.add(new LabelNode());
				toInject.add(new VarInsnNode(ALOAD, 8));
				toInject.add(new TypeInsnNode(CHECKCAST, "appeng/api/config/SortDir"));
				toInject.add(new FieldInsnNode(PUTSTATIC, "com/github/Simon04090/AppliedExchange/core/EMCSorter", "Direction", "Lappeng/api/config/SortDir;"));
				toInject.add(new LdcInsnNode("Set EMCSorter.Direction"));
				toInject.add(new InsnNode(ICONST_0));
				toInject.add(new TypeInsnNode(ANEWARRAY, "java/lang/Object"));
				toInject.add(new MethodInsnNode(INVOKESTATIC, "com/github/Simon04090/AppliedExchange/Logger", "info", "(Ljava/lang/String;[Ljava/lang/Object;)V", false));
				toInject.add(new VarInsnNode(ALOAD, 0));
				toInject.add(new FieldInsnNode(GETFIELD, updateView_CLASS.replace('.', '/'), "view", "Ljava/util/ArrayList;"));
				toInject.add(new FieldInsnNode(GETSTATIC, "com/github/Simon04090/AppliedExchange/core/EMCSorter", "SORT_BY_EMC", "Ljava/util/Comparator;"));
				toInject.add(new MethodInsnNode(INVOKESTATIC, "java/util/Collections", "sort", "(Ljava/util/List;Ljava/util/Comparator;)V", false));
				LabelNode label2 = (LabelNode) targetNode.getNext().getNext().getNext().getNext();
				toInject.add(new JumpInsnNode(GOTO, label2));
				toInject.add(label1);
				toInject.add(new FrameNode(F_SAME, 0, null, 0, null));
			}
			method.instructions.insertBefore(targetNode, toInject);
		}
	}

}