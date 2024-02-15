package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program;

import net.lax1dude.eaglercraft.v1_8.internal.IProgramGL;
import net.lax1dude.eaglercraft.v1_8.internal.IShaderGL;
import net.lax1dude.eaglercraft.v1_8.internal.IUniformGL;

import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL.*;
import static net.lax1dude.eaglercraft.v1_8.opengl.RealOpenGLEnums.*;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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
public class PipelineShaderCloudsSunOcclusion extends ShaderProgram<PipelineShaderCloudsSunOcclusion.Uniforms> {

	public static PipelineShaderCloudsSunOcclusion compile() {
		IShaderGL cloudsOcclusion = ShaderCompiler.compileShader("clouds_sun_occlusion", GL_FRAGMENT_SHADER,
				ShaderSource.clouds_sun_occlusion_fsh);
		try {
			IProgramGL prog = ShaderCompiler.linkProgram("clouds_sun_occlusion", SharedPipelineShaders.deferred_local, cloudsOcclusion);
			return new PipelineShaderCloudsSunOcclusion(prog);
		}finally {
			if(cloudsOcclusion != null) {
				cloudsOcclusion.free();
			}
		}
	}
	private PipelineShaderCloudsSunOcclusion(IProgramGL program) {
		super(program, new Uniforms());
	}

	public static class Uniforms implements IProgramUniforms {

		public IUniformGL u_sampleMatrix4x3f = null;

		private Uniforms() {
		}

		@Override
		public void loadUniforms(IProgramGL prog) {
			u_sampleMatrix4x3f = _wglGetUniformLocation(prog, "u_sampleMatrix4x3f");
			_wglUniform1i(_wglGetUniformLocation(prog, "u_cloudsTexture"), 0);
		}

	}

}
