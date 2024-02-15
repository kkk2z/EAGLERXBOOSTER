package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayerMP;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.Packet;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server.S21PacketChunkData;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.network.play.server.S23PacketBlockChange;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.tileentity.TileEntity;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.BlockPos;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.LongHashMap;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.MathHelper;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.ChunkCoordIntPair;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.WorldProvider;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.WorldServer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.chunk.Chunk;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

import static net.lax1dude.eaglercraft.v1_8.sp.server.classes.ContextUtil.__checkIntegratedContextValid;

/**+
 * This portion of EaglercraftX contains deobfuscated Minecraft 1.8 source code.
 * 
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!"
 * Mod Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class PlayerManager {

	static {
		__checkIntegratedContextValid("net/minecraft/server/management/PlayerManager");
	}

	private static final Logger pmLogger = LogManager.getLogger();
	private final WorldServer theWorldServer;
	/**+
	 * players in the current instance
	 */
	private final List<EntityPlayerMP> players = Lists.newArrayList();
	/**+
	 * the hash of all playerInstances created
	 */
	private final LongHashMap<PlayerManager.PlayerInstance> playerInstances = new LongHashMap();
	/**+
	 * the playerInstances(chunks) that need to be updated
	 */
	private final List<PlayerManager.PlayerInstance> playerInstancesToUpdate = Lists.newArrayList();
	/**+
	 * This field is using when chunk should be processed (every
	 * 8000 ticks)
	 */
	private final List<PlayerManager.PlayerInstance> playerInstanceList = Lists.newArrayList();
	private int playerViewRadius;
	private long previousTotalWorldTime;
	/**+
	 * x, z direction vectors: east, south, west, north
	 */
	private final int[][] xzDirectionsConst = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

	public PlayerManager(WorldServer serverWorld) {
		this.theWorldServer = serverWorld;
		this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
	}

	/**+
	 * Returns the WorldServer associated with this PlayerManager
	 */
	public WorldServer getWorldServer() {
		return this.theWorldServer;
	}

	/**+
	 * updates all the player instances that need to be updated
	 */
	public void updatePlayerInstances() {
		long i = this.theWorldServer.getTotalWorldTime();
		if (i - this.previousTotalWorldTime > 8000L) {
			this.previousTotalWorldTime = i;

			for (int j = 0; j < this.playerInstanceList.size(); ++j) {
				PlayerManager.PlayerInstance playermanager$playerinstance = (PlayerManager.PlayerInstance) this.playerInstanceList
						.get(j);
				playermanager$playerinstance.onUpdate();
				playermanager$playerinstance.processChunk();
			}
		} else {
			for (int k = 0; k < this.playerInstancesToUpdate.size(); ++k) {
				PlayerManager.PlayerInstance playermanager$playerinstance1 = (PlayerManager.PlayerInstance) this.playerInstancesToUpdate
						.get(k);
				playermanager$playerinstance1.onUpdate();
			}
		}

		this.playerInstancesToUpdate.clear();
		if (this.players.isEmpty()) {
			WorldProvider worldprovider = this.theWorldServer.provider;
			if (!worldprovider.canRespawnHere()) {
				this.theWorldServer.theChunkProviderServer.unloadAllChunks();
			}
		}

	}

	public boolean hasPlayerInstance(int chunkX, int chunkZ) {
		long i = (long) chunkX + 2147483647L | (long) chunkZ + 2147483647L << 32;
		return this.playerInstances.getValueByKey(i) != null;
	}

	/**+
	 * passi n the chunk x and y and a flag as to whether or not the
	 * instance should be made if it doesnt exist
	 */
	private PlayerManager.PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent) {
		long i = (long) chunkX + 2147483647L | (long) chunkZ + 2147483647L << 32;
		PlayerManager.PlayerInstance playermanager$playerinstance = (PlayerManager.PlayerInstance) this.playerInstances
				.getValueByKey(i);
		if (playermanager$playerinstance == null && createIfAbsent) {
			playermanager$playerinstance = new PlayerManager.PlayerInstance(chunkX, chunkZ);
			this.playerInstances.add(i, playermanager$playerinstance);
			this.playerInstanceList.add(playermanager$playerinstance);
		}

		return playermanager$playerinstance;
	}

	public void markBlockForUpdate(BlockPos pos) {
		int i = pos.getX() >> 4;
		int j = pos.getZ() >> 4;
		PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i, j, false);
		if (playermanager$playerinstance != null) {
			playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
		}

	}

	/**+
	 * Adds an EntityPlayerMP to the PlayerManager and to all player
	 * instances within player visibility
	 */
	public void addPlayer(EntityPlayerMP player) {
		int i = (int) player.posX >> 4;
		int j = (int) player.posZ >> 4;
		player.managedPosX = player.posX;
		player.managedPosZ = player.posZ;

		for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
			for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
				this.getPlayerInstance(k, l, true).addPlayer(player);
			}
		}

		this.players.add(player);
		this.filterChunkLoadQueue(player);
	}

	/**+
	 * Removes all chunks from the given player's chunk load queue
	 * that are not in viewing range of the player.
	 */
	public void filterChunkLoadQueue(EntityPlayerMP player) {
		ArrayList arraylist = Lists.newArrayList(player.loadedChunks);
		int i = 0;
		int j = this.playerViewRadius;
		int k = (int) player.posX >> 4;
		int l = (int) player.posZ >> 4;
		int i1 = 0;
		int j1 = 0;
		ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoords;
		player.loadedChunks.clear();
		if (arraylist.contains(chunkcoordintpair)) {
			player.loadedChunks.add(chunkcoordintpair);
		}

		for (int k1 = 1; k1 <= j * 2; ++k1) {
			for (int l1 = 0; l1 < 2; ++l1) {
				int[] aint = this.xzDirectionsConst[i++ % 4];

				for (int i2 = 0; i2 < k1; ++i2) {
					i1 += aint[0];
					j1 += aint[1];
					chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;
					if (arraylist.contains(chunkcoordintpair)) {
						player.loadedChunks.add(chunkcoordintpair);
					}
				}
			}
		}

		i = i % 4;

		for (int j2 = 0; j2 < j * 2; ++j2) {
			i1 += this.xzDirectionsConst[i][0];
			j1 += this.xzDirectionsConst[i][1];
			chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;
			if (arraylist.contains(chunkcoordintpair)) {
				player.loadedChunks.add(chunkcoordintpair);
			}
		}

	}

	/**+
	 * Removes an EntityPlayerMP from the PlayerManager.
	 */
	public void removePlayer(EntityPlayerMP player) {
		int i = (int) player.managedPosX >> 4;
		int j = (int) player.managedPosZ >> 4;

		for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
			for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
				PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k, l, false);
				if (playermanager$playerinstance != null) {
					playermanager$playerinstance.removePlayer(player);
				}
			}
		}

		this.players.remove(player);
	}

	/**+
	 * Determine if two rectangles centered at the given points
	 * overlap for the provided radius. Arguments: x1, z1, x2, z2,
	 * radius.
	 */
	private boolean overlaps(int x1, int z1, int x2, int z2, int radius) {
		int i = x1 - x2;
		int j = z1 - z2;
		return i >= -radius && i <= radius ? j >= -radius && j <= radius : false;
	}

	/**+
	 * update chunks around a player being moved by server logic
	 * (e.g. cart, boat)
	 */
	public void updateMountedMovingPlayer(EntityPlayerMP player) {
		int i = (int) player.posX >> 4;
		int j = (int) player.posZ >> 4;
		double d0 = player.managedPosX - player.posX;
		double d1 = player.managedPosZ - player.posZ;
		double d2 = d0 * d0 + d1 * d1;
		if (d2 >= 64.0D) {
			int k = (int) player.managedPosX >> 4;
			int l = (int) player.managedPosZ >> 4;
			int i1 = this.playerViewRadius;
			int j1 = i - k;
			int k1 = j - l;
			if (j1 != 0 || k1 != 0) {
				for (int l1 = i - i1; l1 <= i + i1; ++l1) {
					for (int i2 = j - i1; i2 <= j + i1; ++i2) {
						if (!this.overlaps(l1, i2, k, l, i1)) {
							this.getPlayerInstance(l1, i2, true).addPlayer(player);
						}

						if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1)) {
							PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l1 - j1,
									i2 - k1, false);
							if (playermanager$playerinstance != null) {
								playermanager$playerinstance.removePlayer(player);
							}
						}
					}
				}

				this.filterChunkLoadQueue(player);
				player.managedPosX = player.posX;
				player.managedPosZ = player.posZ;
			}
		}
	}

	public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ) {
		PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkX, chunkZ, false);
		return playermanager$playerinstance != null
				&& playermanager$playerinstance.playersWatchingChunk.contains(player)
				&& !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords);
	}

	public void setPlayerViewRadius(int radius) {
		radius = MathHelper.clamp_int(radius, 3, 32);
		if (radius != this.playerViewRadius) {
			int i = radius - this.playerViewRadius;

			for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players)) {
				int j = (int) entityplayermp.posX >> 4;
				int k = (int) entityplayermp.posZ >> 4;
				if (i > 0) {
					for (int j1 = j - radius; j1 <= j + radius; ++j1) {
						for (int k1 = k - radius; k1 <= k + radius; ++k1) {
							PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(j1, k1,
									true);
							if (!playermanager$playerinstance.playersWatchingChunk.contains(entityplayermp)) {
								playermanager$playerinstance.addPlayer(entityplayermp);
							}
						}
					}
				} else {
					for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
						for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1) {
							if (!this.overlaps(l, i1, j, k, radius)) {
								this.getPlayerInstance(l, i1, true).removePlayer(entityplayermp);
							}
						}
					}
				}
			}

			this.playerViewRadius = radius;
		}
	}

	/**+
	 * Get the furthest viewable block given player's view distance
	 */
	public static int getFurthestViewableBlock(int distance) {
		return distance * 16 - 16;
	}

	class PlayerInstance {
		private final List<EntityPlayerMP> playersWatchingChunk = Lists.newArrayList();
		private final ChunkCoordIntPair chunkCoords;
		private short[] locationOfBlockChange = new short[64];
		private int numBlocksToUpdate;
		private int flagsYAreasToUpdate;
		private long previousWorldTime;

		public PlayerInstance(int chunkX, int chunkZ) {
			this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
			PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ);
		}

		/**+
		 * Adds an EntityPlayerMP to the PlayerManager and to all player
		 * instances within player visibility
		 */
		public void addPlayer(EntityPlayerMP player) {
			if (this.playersWatchingChunk.contains(player)) {
				PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}",
						new Object[] { player, Integer.valueOf(this.chunkCoords.chunkXPos),
								Integer.valueOf(this.chunkCoords.chunkZPos) });
			} else {
				if (this.playersWatchingChunk.isEmpty()) {
					this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
				}

				this.playersWatchingChunk.add(player);
				player.loadedChunks.add(this.chunkCoords);
			}
		}

		/**+
		 * Removes an EntityPlayerMP from the PlayerManager.
		 */
		public void removePlayer(EntityPlayerMP player) {
			if (this.playersWatchingChunk.contains(player)) {
				Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos,
						this.chunkCoords.chunkZPos);
				if (chunk.isPopulated()) {
					player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
				}

				this.playersWatchingChunk.remove(player);
				player.loadedChunks.remove(this.chunkCoords);
				if (this.playersWatchingChunk.isEmpty()) {
					long i = (long) this.chunkCoords.chunkXPos + 2147483647L
							| (long) this.chunkCoords.chunkZPos + 2147483647L << 32;
					this.increaseInhabitedTime(chunk);
					PlayerManager.this.playerInstances.remove(i);
					PlayerManager.this.playerInstanceList.remove(this);
					if (this.numBlocksToUpdate > 0) {
						PlayerManager.this.playerInstancesToUpdate.remove(this);
					}

					PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos,
							this.chunkCoords.chunkZPos);
				}

			}
		}

		public void processChunk() {
			this.increaseInhabitedTime(PlayerManager.this.theWorldServer
					.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
		}

		private void increaseInhabitedTime(Chunk theChunk) {
			theChunk.setInhabitedTime(theChunk.getInhabitedTime()
					+ PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
			this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
		}

		public void flagChunkForUpdate(int x, int y, int z) {
			if (this.numBlocksToUpdate == 0) {
				PlayerManager.this.playerInstancesToUpdate.add(this);
			}

			this.flagsYAreasToUpdate |= 1 << (y >> 4);
			if (this.numBlocksToUpdate < 64) {
				short short1 = (short) (x << 12 | z << 8 | y);

				for (int i = 0; i < this.numBlocksToUpdate; ++i) {
					if (this.locationOfBlockChange[i] == short1) {
						return;
					}
				}

				this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
			}

		}

		public void sendToAllPlayersWatchingChunk(Packet thePacket) {
			for (int i = 0; i < this.playersWatchingChunk.size(); ++i) {
				EntityPlayerMP entityplayermp = (EntityPlayerMP) this.playersWatchingChunk.get(i);
				if (!entityplayermp.loadedChunks.contains(this.chunkCoords)) {
					entityplayermp.playerNetServerHandler.sendPacket(thePacket);
				}
			}

		}

		public void onUpdate() {
			if (this.numBlocksToUpdate != 0) {
				if (this.numBlocksToUpdate == 1) {
					int i = (this.locationOfBlockChange[0] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
					int j = this.locationOfBlockChange[0] & 255;
					int k = (this.locationOfBlockChange[0] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
					BlockPos blockpos = new BlockPos(i, j, k);
					this.sendToAllPlayersWatchingChunk(
							new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));
					if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity()) {
						this.sendTileToAllPlayersWatchingChunk(
								PlayerManager.this.theWorldServer.getTileEntity(blockpos));
					}
				} else if (this.numBlocksToUpdate == 64) {
					int i1 = this.chunkCoords.chunkXPos * 16;
					int k1 = this.chunkCoords.chunkZPos * 16;
					this.sendToAllPlayersWatchingChunk(
							new S21PacketChunkData(
									PlayerManager.this.theWorldServer.getChunkFromChunkCoords(
											this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos),
									false, this.flagsYAreasToUpdate));

					for (int i2 = 0; i2 < 16; ++i2) {
						if ((this.flagsYAreasToUpdate & 1 << i2) != 0) {
							int k2 = i2 << 4;
							List list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i1, k2, k1, i1 + 16,
									k2 + 16, k1 + 16);

							for (int l = 0; l < list.size(); ++l) {
								this.sendTileToAllPlayersWatchingChunk((TileEntity) list.get(l));
							}
						}
					}
				} else {
					this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate,
							this.locationOfBlockChange, PlayerManager.this.theWorldServer
									.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));

					for (int j1 = 0; j1 < this.numBlocksToUpdate; ++j1) {
						int l1 = (this.locationOfBlockChange[j1] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
						int j2 = this.locationOfBlockChange[j1] & 255;
						int l2 = (this.locationOfBlockChange[j1] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
						BlockPos blockpos1 = new BlockPos(l1, j2, l2);
						if (PlayerManager.this.theWorldServer.getBlockState(blockpos1).getBlock().hasTileEntity()) {
							this.sendTileToAllPlayersWatchingChunk(
									PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
						}
					}
				}

				this.numBlocksToUpdate = 0;
				this.flagsYAreasToUpdate = 0;
			}
		}

		private void sendTileToAllPlayersWatchingChunk(TileEntity theTileEntity) {
			if (theTileEntity != null) {
				Packet packet = theTileEntity.getDescriptionPacket();
				if (packet != null) {
					this.sendToAllPlayersWatchingChunk(packet);
				}
			}

		}
	}
}