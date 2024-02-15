package net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.potion;

import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.ai.attributes.AttributeModifier;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.potion.Potion;
import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ResourceLocation;

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
public class PotionAttackDamage extends Potion {

	static {
		__checkIntegratedContextValid("net/minecraft/potion/PotionAttackDamage");
	}

	protected PotionAttackDamage(int potionID, ResourceLocation location, boolean badEffect, int potionColor) {
		super(potionID, location, badEffect, potionColor);
	}

	public double getAttributeModifierAmount(int modifier, AttributeModifier parAttributeModifier) {
		return this.id == Potion.weakness.id ? (double) (-0.5F * (float) (modifier + 1))
				: 1.3D * (double) (modifier + 1);
	}
}