/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.receiver.trace.mock;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.apache.skywalking.apm.network.language.agent.RefType;
import org.apache.skywalking.apm.network.language.agent.SpanLayer;
import org.apache.skywalking.apm.network.language.agent.SpanType;
import org.apache.skywalking.apm.network.language.agent.UniqueId;
import org.apache.skywalking.apm.network.language.agent.UpstreamSegment;
import org.apache.skywalking.apm.network.language.agent.v2.SegmentObject;
import org.apache.skywalking.apm.network.language.agent.v2.SegmentReference;
import org.apache.skywalking.apm.network.language.agent.v2.SpanObjectV2;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

class ServiceCMock {

    private final RegisterMock registerMock;
    private static int SERVICE_ID;
    private static int SERVICE_INSTANCE_ID;

    ServiceCMock(RegisterMock registerMock) {
        this.registerMock = registerMock;
    }

    void register() throws InterruptedException {
        SERVICE_ID = registerMock.registerService("rocket-mq-consumer");
        SERVICE_INSTANCE_ID = registerMock.registerServiceInstance(SERVICE_ID, "pengysC");
    }

    void mock(StreamObserver<UpstreamSegment> streamObserver,
              UniqueId.Builder traceId,
              UniqueId.Builder segmentId,
              UniqueId.Builder parentTraceSegmentId,
              long startTimestamp,
              boolean isPrepare) {
        UpstreamSegment.Builder upstreamSegment = UpstreamSegment.newBuilder();
        upstreamSegment.addGlobalTraceIds(traceId);
        upstreamSegment.setSegment(createSegment(startTimestamp, segmentId, parentTraceSegmentId, isPrepare));

        streamObserver.onNext(upstreamSegment.build());
    }

    private ByteString createSegment(long startTimestamp, UniqueId.Builder segmentId,
                                     UniqueId.Builder parentTraceSegmentId, boolean isPrepare) {
        SegmentObject.Builder segment = SegmentObject.newBuilder();
        segment.setTraceSegmentId(segmentId);
        segment.setServiceInstanceId(SERVICE_INSTANCE_ID);
        segment.setServiceId(SERVICE_ID);
        segment.addSpans(createEntrySpan(startTimestamp, parentTraceSegmentId, isPrepare));

        return segment.build().toByteString();
    }

    private SpanObjectV2.Builder createEntrySpan(long startTimestamp, UniqueId.Builder uniqueId, boolean isPrepare) {
        SpanObjectV2.Builder span = SpanObjectV2.newBuilder();
        span.setSpanId(0);
        span.setSpanType(SpanType.Entry);
        span.setSpanLayer(SpanLayer.MQ);
        span.setParentSpanId(-1);
        span.setStartTime(startTimestamp + 3000);
        span.setEndTime(startTimestamp + 5000);
        span.setComponentId(ComponentsDefine.ROCKET_MQ_CONSUMER.getId());
        span.setIsError(false);
        span.addRefs(createReference(uniqueId, isPrepare));

        if (isPrepare) {
            span.setOperationName(ServiceBMock.ROCKET_MQ_ENDPOINT);
        } else {
            span.setOperationNameId(5);
        }
        return span;
    }

    private SegmentReference.Builder createReference(UniqueId.Builder parentTraceSegmentId, boolean isPrepare) {
        SegmentReference.Builder reference = SegmentReference.newBuilder();
        reference.setParentTraceSegmentId(parentTraceSegmentId);
        reference.setParentServiceInstanceId(ServiceBMock.SERVICE_INSTANCE_ID);
        reference.setParentSpanId(2);
        reference.setEntryServiceInstanceId(ServiceAMock.SERVICE_INSTANCE_ID);
        reference.setRefType(RefType.CrossProcess);

        if (isPrepare) {
            reference.setParentEndpoint(ServiceBMock.DUBBO_PROVIDER_ENDPOINT);
            reference.setNetworkAddress(ServiceBMock.ROCKET_MQ_ADDRESS);
            reference.setEntryEndpoint(ServiceAMock.REST_ENDPOINT);
        } else {
            reference.setParentEndpointId(8);
            reference.setNetworkAddressId(4);
            reference.setEntryEndpointId(2);
        }
        return reference;
    }
}