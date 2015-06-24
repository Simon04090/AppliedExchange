package com.github.Simon04090.AppliedExchange.core;

import java.math.BigInteger;
import java.util.Comparator;
import moze_intel.projecte.utils.EMCHelper;
import appeng.api.config.SortDir;
import appeng.api.storage.data.IAEItemStack;
import appeng.util.Platform;

public class EMCSorter{
	public static SortDir Direction = SortDir.ASCENDING;
	public static final Comparator<IAEItemStack> SORT_BY_EMC = new Comparator<IAEItemStack>(){

		@Override
		public int compare(IAEItemStack o1, IAEItemStack o2){
			// Logger.info("Comparing by EMC");
			int itememc1 = EMCHelper.getEmcValue(o1.getItemStack());
			BigInteger stack1 = BigInteger.valueOf(itememc1).multiply(BigInteger.valueOf(o1.getStackSize()));
			// Logger.info(o1.getItemStack().getDisplayName() +
			// stack1.toString());
			int stackemc1;
			if (stack1.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0){
				// Logger.info("Value is bigger than Integer.MAX_VALUE");
				stackemc1 = Integer.MAX_VALUE;
				// Logger.info(String.valueOf(stackemc1));
			} else{
				stackemc1 = stack1.intValue();
			}
			int itememc2 = EMCHelper.getEmcValue(o2.getItemStack());
			BigInteger stack2 = BigInteger.valueOf(itememc2).multiply(BigInteger.valueOf(o2.getStackSize()));
			int stackemc2;
			if (stack2.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0){
				stackemc2 = Integer.MAX_VALUE;
			} else{
				stackemc2 = stack2.intValue();
			}

			// Logger.info("FirstStack:" + String.valueOf(stackemc1));
			// Logger.info("SecondStack:" + String.valueOf(stackemc2));

			if (Direction == SortDir.ASCENDING){
				// Logger.info(String.valueOf(compareInt(stackemc2,
				// stackemc1)));
				// return compareInt(stackemc2, stackemc1);
				return this.secondarySort(compareInt(stackemc2, stackemc1), o1, o2);
			}
			// Logger.info(String.valueOf(compareInt(stackemc1, stackemc2)));
			// return compareInt(stackemc1, stackemc2);
			return this.secondarySort(compareInt(stackemc1, stackemc2), o2, o1);
		}

		private int secondarySort(int compareInt, IAEItemStack o1, IAEItemStack o2){
			if (compareInt == 0){
				return Platform.getItemDisplayName(o2).compareToIgnoreCase(Platform.getItemDisplayName(o1));
			}
			return compareInt;
		}
	};

	public static int compareInt(int a, int b){
		if (a == b){
			// Logger.info(String.valueOf(a) + "=" + String.valueOf(b));
			return 0;
		}
		if (a < b){
			// Logger.info(String.valueOf(a) + "<" + String.valueOf(b));
			return -1;
		}
		// Logger.info(String.valueOf(a) + ">" + String.valueOf(b));
		return 1;
	}
}
