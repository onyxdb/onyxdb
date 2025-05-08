FROM ubuntu:20.04

# Install logrotate and dependencies
RUN apt-get update && apt-get install -y \
    logrotate \
    cron \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Configure logrotate with specific settings
COPY ./docker/logrotate/mongod /etc/logrotate.d/

# Set up cron to run logrotate hourly
RUN echo '0 9 * * * /usr/sbin/logrotate -f /etc/logrotate.conf >/proc/1/fd/1 2>/proc/1/fd/2' > /etc/cron.d/logrotate-cron \
    && chmod 0644 /etc/cron.d/logrotate-cron \
    && crontab /etc/cron.d/logrotate-cron

# Create log directories with appropriate permissions
RUN mkdir -p /var/log/app \
    && touch /var/log/logrotate.status \
    && chmod 644 /var/log/logrotate.status

CMD cron && tail -f /dev/null

#COPY hello-cron /etc/cron.d/hello-cron
#
## Give execution rights on the cron job
#RUN chmod 0644 /etc/cron.d/hello-cron
#
## Apply cron job
#RUN crontab /etc/cron.d/hello-cron
#
## Create the log file to be able to run tail
#RUN touch /var/log/cron.log
#
## Run the command on container startup
#CMD cron && tail -f /var/log/cron.log


## Install logrotate and dependencies
#RUN apt-get update && apt-get install -y \
#    logrotate \
#    cron \
#    ca-certificates \
#    && rm -rf /var/lib/apt/lists/*
#
## Configure logrotate with specific settings
#COPY ./docker/logrotate/mongod.conf /etc/logrotate.d/
#
## Set up cron to run logrotate hourly
#RUN echo '* * * * * /usr/sbin/logrotate -f /etc/logrotate.conf >/proc/1/fd/1 2>/proc/1/fd/2' > /etc/cron.d/logrotate-cron \
#    && chmod 0644 /etc/cron.d/logrotate-cron \
#    && crontab /etc/cron.d/logrotate-cron
#
## Create log directories with appropriate permissions
#RUN mkdir -p /var/log/app \
#    && touch /var/log/logrotate.status \
#    && chmod 644 /var/log/logrotate.status
#
## Start both cron daemon and application
#ENTRYPOINT ["/bin/bash", "-c", "service cron start"]
##CMD ["cron", "-f"]
