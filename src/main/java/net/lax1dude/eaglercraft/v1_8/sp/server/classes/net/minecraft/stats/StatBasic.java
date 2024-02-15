package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.stats;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.stats.IStatType;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.stats.StatBase;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.stats.StatList;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.IChatComponent;

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
public class StatBasic extends StatBase {

	static {
		__checkIntegratedContextValid("net/minecraft/stats/StatBasic");
	}

	public StatBasic(String statIdIn, IChatComponent statNameIn, IStatType typeIn) {
		super(statIdIn, statNameIn, typeIn);
	}

	public StatBasic(String statIdIn, IChatComponent statNameIn) {
		super(statIdIn, statNameIn);
	}

	/**+
	 * Register the stat into StatList.
	 */
	public StatBase registerStat() {
		super.registerStat();
		StatList.generalStats.add(this);
		return this;
	}
}