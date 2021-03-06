package mca.ai;

import java.util.ArrayList;
import java.util.List;

import mca.entity.EntityHuman;
import net.minecraft.nbt.NBTTagCompound;

public class AIManager 
{
	private EntityHuman owner;
	private List<AbstractAI> AIList;

	public AIManager(EntityHuman owner)
	{
		this.owner = owner;
		this.AIList = new ArrayList<AbstractAI>();
	}

	public void addAI(AbstractAI AI)
	{
		AIList.add(AI);
	}

	public void onUpdate()
	{
		for (final AbstractAI AI : AIList)
		{
			boolean doRun = AI instanceof AbstractToggleAI ? ((AbstractToggleAI)AI).getIsActive() : true;

			if (doRun)
			{
				AI.onUpdateCommon();

				if (owner.worldObj.isRemote)
				{
					AI.onUpdateClient();
				}

				else
				{
					AI.onUpdateServer();
				}
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		for (final AbstractAI AI : AIList)
		{
			AI.writeToNBT(nbt);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		for (final AbstractAI AI : AIList)
		{
			AI.readFromNBT(nbt);
		}
	}

	public <T extends AbstractAI> T getAI(Class<T> clazz)
	{
		for (final AbstractAI AI : AIList)
		{
			if (AI.getClass() == clazz)
			{
				return (T) AI;
			}
		}

		return null;
	}
	
	public boolean isToggleAIActive()
	{
		for (final AbstractAI AI : AIList)
		{
			if (AI instanceof AbstractToggleAI)
			{
				AbstractToggleAI TAI = (AbstractToggleAI) AI;
				
				if (TAI.getIsActive())
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String getNameOfActiveAI()
	{
		for (final AbstractAI AI : AIList)
		{
			if (AI instanceof AbstractToggleAI)
			{
				AbstractToggleAI TAI = (AbstractToggleAI) AI;
				
				if (TAI.getIsActive())
				{
					return TAI.getName();
				}
			}
		}
		
		return "";
	}
	
	public void disableAllToggleAIs()
	{
		for (final AbstractAI AI : AIList)
		{
			if (AI instanceof AbstractToggleAI)
			{
				AbstractToggleAI TAI = (AbstractToggleAI) AI;
				TAI.setIsActive(false);
			}
		}
	}
}
