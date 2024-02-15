package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.gen.layer;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.biome.BiomeGenBase;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.gen.layer.GenLayer;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.world.gen.layer.IntCache;
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
public class GenLayerHills extends GenLayer {

	static {
		__checkIntegratedContextValid("net/minecraft/world/gen/layer/GenLayerHills");
	}

	private static final Logger logger = LogManager.getLogger();
	private GenLayer field_151628_d;

	public GenLayerHills(long parLong1, GenLayer parGenLayer, GenLayer parGenLayer2) {
		super(parLong1);
		this.parent = parGenLayer;
		this.field_151628_d = parGenLayer2;
	}

	/**+
	 * Returns a list of integer values generated by this layer.
	 * These may be interpreted as temperatures, rainfall amounts,
	 * or biomeList[] indices based on the particular GenLayer
	 * subclass.
	 */
	public int[] getInts(int i, int j, int k, int l) {
		int[] aint = this.parent.getInts(i - 1, j - 1, k + 2, l + 2);
		int[] aint1 = this.field_151628_d.getInts(i - 1, j - 1, k + 2, l + 2);
		int[] aint2 = IntCache.getIntCache(k * l);

		for (int i1 = 0; i1 < l; ++i1) {
			for (int j1 = 0; j1 < k; ++j1) {
				this.initChunkSeed((long) (j1 + i), (long) (i1 + j));
				int k1 = aint[j1 + 1 + (i1 + 1) * (k + 2)];
				int l1 = aint1[j1 + 1 + (i1 + 1) * (k + 2)];
				boolean flag = (l1 - 2) % 29 == 0;
				if (k1 > 255) {
					logger.debug("old! " + k1);
				}

				if (k1 != 0 && l1 >= 2 && (l1 - 2) % 29 == 1 && k1 < 128) {
					if (BiomeGenBase.getBiome(k1 + 128) != null) {
						aint2[j1 + i1 * k] = k1 + 128;
					} else {
						aint2[j1 + i1 * k] = k1;
					}
				} else if (this.nextInt(3) != 0 && !flag) {
					aint2[j1 + i1 * k] = k1;
				} else {
					int i2 = k1;
					if (k1 == BiomeGenBase.desert.biomeID) {
						i2 = BiomeGenBase.desertHills.biomeID;
					} else if (k1 == BiomeGenBase.forest.biomeID) {
						i2 = BiomeGenBase.forestHills.biomeID;
					} else if (k1 == BiomeGenBase.birchForest.biomeID) {
						i2 = BiomeGenBase.birchForestHills.biomeID;
					} else if (k1 == BiomeGenBase.roofedForest.biomeID) {
						i2 = BiomeGenBase.plains.biomeID;
					} else if (k1 == BiomeGenBase.taiga.biomeID) {
						i2 = BiomeGenBase.taigaHills.biomeID;
					} else if (k1 == BiomeGenBase.megaTaiga.biomeID) {
						i2 = BiomeGenBase.megaTaigaHills.biomeID;
					} else if (k1 == BiomeGenBase.coldTaiga.biomeID) {
						i2 = BiomeGenBase.coldTaigaHills.biomeID;
					} else if (k1 == BiomeGenBase.plains.biomeID) {
						if (this.nextInt(3) == 0) {
							i2 = BiomeGenBase.forestHills.biomeID;
						} else {
							i2 = BiomeGenBase.forest.biomeID;
						}
					} else if (k1 == BiomeGenBase.icePlains.biomeID) {
						i2 = BiomeGenBase.iceMountains.biomeID;
					} else if (k1 == BiomeGenBase.jungle.biomeID) {
						i2 = BiomeGenBase.jungleHills.biomeID;
					} else if (k1 == BiomeGenBase.ocean.biomeID) {
						i2 = BiomeGenBase.deepOcean.biomeID;
					} else if (k1 == BiomeGenBase.extremeHills.biomeID) {
						i2 = BiomeGenBase.extremeHillsPlus.biomeID;
					} else if (k1 == BiomeGenBase.savanna.biomeID) {
						i2 = BiomeGenBase.savannaPlateau.biomeID;
					} else if (biomesEqualOrMesaPlateau(k1, BiomeGenBase.mesaPlateau_F.biomeID)) {
						i2 = BiomeGenBase.mesa.biomeID;
					} else if (k1 == BiomeGenBase.deepOcean.biomeID && this.nextInt(3) == 0) {
						int j2 = this.nextInt(2);
						if (j2 == 0) {
							i2 = BiomeGenBase.plains.biomeID;
						} else {
							i2 = BiomeGenBase.forest.biomeID;
						}
					}

					if (flag && i2 != k1) {
						if (BiomeGenBase.getBiome(i2 + 128) != null) {
							i2 += 128;
						} else {
							i2 = k1;
						}
					}

					if (i2 == k1) {
						aint2[j1 + i1 * k] = k1;
					} else {
						int k3 = aint[j1 + 1 + (i1 + 1 - 1) * (k + 2)];
						int k2 = aint[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
						int l2 = aint[j1 + 1 - 1 + (i1 + 1) * (k + 2)];
						int i3 = aint[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
						int j3 = 0;
						if (biomesEqualOrMesaPlateau(k3, k1)) {
							++j3;
						}

						if (biomesEqualOrMesaPlateau(k2, k1)) {
							++j3;
						}

						if (biomesEqualOrMesaPlateau(l2, k1)) {
							++j3;
						}

						if (biomesEqualOrMesaPlateau(i3, k1)) {
							++j3;
						}

						if (j3 >= 3) {
							aint2[j1 + i1 * k] = i2;
						} else {
							aint2[j1 + i1 * k] = k1;
						}
					}
				}
			}
		}

		return aint2;
	}
}