package co.elastic.apm.mule4.agent;

import org.mule.runtime.api.notification.PipelineMessageNotification;
import org.mule.runtime.api.notification.PipelineMessageNotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Listener for Mule notifications on flow start, end and completion.
 */
public class ApmPipelineNotificationListener
		implements PipelineMessageNotificationListener<PipelineMessageNotification> {

	private ApmHandler apmHandler;

	private Logger logger = LoggerFactory.getLogger(ApmPipelineNotificationListener.class);

	public ApmPipelineNotificationListener(ApmHandler apmHandler) {
		this.apmHandler = apmHandler;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onNotification(PipelineMessageNotification notification) {
		logger.debug("===> Received " + notification.getClass().getName() + ":" + notification.getActionName());

		// Event listener
		// TODO: refactor to remove the deprecation warning.
		switch (notification.getAction().getActionId()) {
		case PipelineMessageNotification.PROCESS_START:
			apmHandler.handleFlowStartEvent(notification);
			break;

		// On exception this event doesn't fire, only on successful flow completion.
		case PipelineMessageNotification.PROCESS_END:
			break;
			
		case PipelineMessageNotification.PROCESS_COMPLETE:
			apmHandler.handleFlowEndEvent(notification);
			break;
		}
	}

}
