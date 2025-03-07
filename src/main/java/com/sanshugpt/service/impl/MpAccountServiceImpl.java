package com.sanshugpt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sanshugpt.module.mp.dao.MpAccountDao;
import com.sanshugpt.module.mp.entity.MpAccount;
import com.sanshugpt.service.MpAccountService;
import org.springframework.stereotype.Service;

/**
* @author sanshu
* @description 针对表【mp_account(公众号账号管理)】的数据库操作Service实现
* @createDate 2025-03-07 14:09:15
*/
@Service
public class MpAccountServiceImpl extends ServiceImpl<MpAccountDao, MpAccount>
    implements MpAccountService{

}




