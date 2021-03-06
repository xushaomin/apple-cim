/**
* 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.appleframework.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.appleframework.cim.sdk.client.filter;

import org.apache.log4j.Logger;

import com.appleframework.cim.sdk.client.constant.CIMConstant;
import com.appleframework.cim.sdk.client.model.Protobufable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 客户端消息发送前进行编码,可在此加密消息
 *
 */
public class ClientMessageEncoder extends MessageToByteEncoder<Object> {
	protected final Logger logger = Logger.getLogger(ClientMessageEncoder.class.getSimpleName());

	@Override
	protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf out) throws Exception {
		if (message instanceof Protobufable) {
			Protobufable data = (Protobufable) message;
			byte[] byteArray = data.getByteArray();
			out.writeBytes(createHeader(data.getType(), byteArray.length));
			out.writeBytes(byteArray);
		}
		logger.info(message.toString());
	}

	/**
	 * 消息体最大为65535
	 * 
	 * @param type
	 * @param length
	 * @return
	 */
	private byte[] createHeader(byte type, int length) {
		byte[] header = new byte[CIMConstant.DATA_HEADER_LENGTH];
		header[0] = type;
		header[1] = (byte) (length & 0xff);
		header[2] = (byte) ((length >> 8) & 0xff);
		return header;
	}

}
