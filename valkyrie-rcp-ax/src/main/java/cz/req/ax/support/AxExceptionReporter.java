/*
 * Copyright 2009-2013 Ondrej Burianek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax.support;

import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorReporter;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class AxExceptionReporter implements ErrorReporter, BeanNameAware, InitializingBean {

    private MessageSourceAccessor messageSourceAccessor;
    private String id = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void setBeanName(String beanName) {
        if (getId() == null) {
            setId(beanName);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (messageSourceAccessor == null) {
            //MIG
//            messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services().getService(
//                    MessageSourceAccessor.class);
        }
    }

    @Override
    public void reportError(ErrorInfo info) {
//        String mailTo = getMessageByKeySuffix(".mailTo");
//        if (!StringUtils.isEmpty(mailTo)) {
//            boolean doOutlookWorkaround = false;
//            if (outlookWorkaroundEnabled) {
//                boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
//                if (isWindows) {
//                    doOutlookWorkaround = JOptionPane.showConfirmDialog(null,
//                            getMessageByKeySuffix(".isOutlook.message"),
//                            getMessageByKeySuffix(".isOutlook.title"),
//                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
//                            == JOptionPane.YES_OPTION;
//                }
//            }
//            String[] mailToTokens = mailTo.split(";");
//            List<String> toAddrs = new ArrayList<String>(mailToTokens.length);
//            for (String mailToToken : mailToTokens)
//            {
//                String trimmedMailToToken = mailToToken.trim();
//                if (!StringUtils.isEmpty(trimmedMailToToken)) {
//                    if (doOutlookWorkaround) {
//                        // The standard is no prefix SMTP
//                        // Outlook Express supposidly works with or without prefix SMTP
//                        // Outlook (like in Office) works only with prefix SMTP
//                        // Thunderbird works always without prefix SMTP.
//                        // Thunderbirds works sometimes with prefix SMTP: it even differs from Vista to Vista
//                        trimmedMailToToken = "SMTP:" + trimmedMailToToken;
//                    }
//                    toAddrs.add(trimmedMailToToken);
//                }
//            }
//            mail.setToAddrs(toAddrs);
//        }
//
//        Throwable errorException = info.getErrorException();
//        Object[] messageParams;
//        if (errorException != null) {
//            messageParams = new Object[] {
//                errorException,
//                getStackTraceString(errorException)
//            };
//        } else {
//            messageParams = new Object[] {
//                info.getBasicErrorMessage(),
//                info.getDetailedErrorMessage()
//            };
//        }
//
//        String subject = getMessageByKeySuffix(".subject", messageParams);
//        mail.setSubject(subject);
//
//        String body = getMessageByKeySuffix(".body", messageParams);
//        mail.setBody(body);
//
//        try {
//            Desktop.mail(mail);
//        } catch (LinkageError e) {
//            // Thrown by JDIC 0.9.3 on linux (and probably windows too) when native jars are not used properly
//            String message = getMessageByKeySuffix(".noNativeJdic");
//            throw new IllegalStateException(message, e);
//        } catch (NullPointerException e) {
//            String message = getMessageByKeySuffix(".noDefaultMailClient");
//            throw new IllegalStateException(message, e);
//        } catch (DesktopException e) {
//            String message = getMessageByKeySuffix(".mailException");
//            throw new IllegalStateException(message, e);
//        }
    }

    protected String getMessageByKeySuffix(String keySuffix) {
        return getMessageByKeySuffix(keySuffix, null);
    }

    protected String getMessageByKeySuffix(String keySuffix, Object[] params) {
        List<String> messageKeyList = new ArrayList<String>();
        if (getId() != null) {
            messageKeyList.add(getId() + keySuffix);
        }
        messageKeyList.add("jdicEmailNotifierErrorReporter" + keySuffix);
        messageKeyList.add("emailNotifierErrorReporter" + keySuffix);
        String[] messagesKeys = messageKeyList.toArray(new String[messageKeyList.size()]);
        return messageSourceAccessor.getMessage(new DefaultMessageSourceResolvable(
                messagesKeys, params, messagesKeys[0]));
    }

    protected String getStackTraceString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        t.printStackTrace(printWriter);
        printWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }
}